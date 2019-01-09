package com.jun90.projects.scan.web.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.opencv.core.Core;

import com.google.gson.JsonObject;
import com.jun90.projects.scan.support.AdjustmentScanTask;
import com.jun90.projects.scan.support.CorrectionScanTask;
import com.jun90.projects.scan.support.CroppingScanTask;
import com.jun90.projects.scan.support.ImageScanner;
import com.jun90.projects.scan.support.MirrorScanTask;
import com.jun90.projects.scan.support.RelativePoint;
import com.jun90.projects.scan.support.RotatingScanTask;
import com.jun90.projects.scan.support.ZoomScanTask;
import com.jun90.projects.scan.web.APIUtils;
import com.jun90.projects.scan.web.DatabaseUtils;
import com.jun90.projects.scan.web.KeyUtils;
import com.jun90.projects.scan.web.ResponseCodeUtils;
import com.jun90.projects.scan.web.dao.ImageDAO;
import com.jun90.projects.scan.web.dao.ProjectDAO;
import com.jun90.projects.scan.web.filter.APIFilter;
import com.jun90.projects.scan.web.filter.AuthFilter;
import com.jun90.projects.scan.web.filter.TokenFilter;
import com.jun90.projects.scan.web.model.Image;
import com.jun90.projects.scan.web.model.Project;
import com.jun90.projects.scan.web.model.Token;


public class ProjectServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9164173110148689607L;
	
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	/**
     * @see HttpServlet#HttpServlet()
     */
    public ProjectServlet() {
        super();
    }
    
    private byte[] bufferedImageToByteArray(BufferedImage image) throws IOException {
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	ImageIO.write(image, "jpeg", out);
    	byte[] data = out.toByteArray();
    	out.close();
    	return data;
    }
    
    private BufferedImage byteArrayToBufferedImage(byte[] data) throws IOException {
    	ByteArrayInputStream in = new ByteArrayInputStream(data);
    	BufferedImage image = ImageIO.read(in);
    	in.close();
    	return image;
    }
    
    private BufferedImage makeThumbnail(BufferedImage image, int thumbnailMaxSize) throws IOException {
    	double scale = 1;
		if(image.getWidth() > image.getHeight()) {
			scale = thumbnailMaxSize * 1.0 / image.getWidth();
		} else {
			scale = thumbnailMaxSize * 1.0 / image.getHeight();
		}
		BufferedImage thumbnail = null;
		if(scale == 1) {
			thumbnail = image;
		} else {
			ImageScanner<BufferedImage> scanner = new ImageScanner<BufferedImage>(BufferedImage.class);
			scanner.addTask(new ZoomScanTask(scale, scale));
			thumbnail = scanner.run(image);
		}
    	return thumbnail;
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tokenKey = null;
		String apiKey = null;
		String projectId = null;
		String name = null;
		String action = null;
		String contrast = null, brightness = null;
		String mirror_x = null, mirror_y = null;
		String angle = null;
		String x1 = null, y1 = null, width = null, height = null;
		String lt_x = null, lt_y = null, rt_x = null, rt_y = null,
				rb_x = null, rb_y = null, lb_x = null, lb_y = null;
		BufferedImage image = null;
		if(ServletFileUpload.isMultipartContent(request)) {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
			servletFileUpload.setHeaderEncoding("utf-8");
			servletFileUpload.setFileSizeMax(128 * 1024 * 1024);
			servletFileUpload.setSizeMax(128 * 1024 * 1024);
			try {
				List<FileItem> fileItemList = servletFileUpload.parseRequest(new ServletRequestContext(request));
				for(FileItem fileItem : fileItemList) {
					if(fileItem.isFormField()) {
						if(fileItem.getFieldName().equalsIgnoreCase("token")) tokenKey = fileItem.getString("utf-8");
						else if(fileItem.getFieldName().equalsIgnoreCase("apikey")) apiKey = fileItem.getString("utf-8");
						else if(fileItem.getFieldName().equalsIgnoreCase("project")) projectId = fileItem.getString("utf-8");
						else if(fileItem.getFieldName().equalsIgnoreCase("name")) name = fileItem.getString("utf-8");
						else if(fileItem.getFieldName().equalsIgnoreCase("action")) action = fileItem.getString("utf-8");

						else if(fileItem.getFieldName().equalsIgnoreCase("contrast")) contrast = fileItem.getString("utf-8");
						else if(fileItem.getFieldName().equalsIgnoreCase("brightness")) brightness = fileItem.getString("utf-8");
						else if(fileItem.getFieldName().equalsIgnoreCase("mirror_x")) mirror_x = fileItem.getString("utf-8");
						else if(fileItem.getFieldName().equalsIgnoreCase("mirror_y")) mirror_y = fileItem.getString("utf-8");
						else if(fileItem.getFieldName().equalsIgnoreCase("angle")) angle = fileItem.getString("utf-8");
						else if(fileItem.getFieldName().equalsIgnoreCase("x1")) x1 = fileItem.getString("utf-8");
						else if(fileItem.getFieldName().equalsIgnoreCase("y1")) y1 = fileItem.getString("utf-8");
						else if(fileItem.getFieldName().equalsIgnoreCase("width")) width = fileItem.getString("utf-8");
						else if(fileItem.getFieldName().equalsIgnoreCase("height")) height = fileItem.getString("utf-8");
						else if(fileItem.getFieldName().equalsIgnoreCase("lt_x")) lt_x = fileItem.getString("utf-8");
						else if(fileItem.getFieldName().equalsIgnoreCase("lt_y")) lt_y = fileItem.getString("utf-8");
						else if(fileItem.getFieldName().equalsIgnoreCase("rt_x")) rt_x = fileItem.getString("utf-8");
						else if(fileItem.getFieldName().equalsIgnoreCase("rt_y")) rt_y = fileItem.getString("utf-8");
						else if(fileItem.getFieldName().equalsIgnoreCase("rb_x")) rb_x = fileItem.getString("utf-8");
						else if(fileItem.getFieldName().equalsIgnoreCase("rb_y")) rb_y = fileItem.getString("utf-8");
						else if(fileItem.getFieldName().equalsIgnoreCase("lb_x")) lb_x = fileItem.getString("utf-8");
						else if(fileItem.getFieldName().equalsIgnoreCase("lb_y")) lb_y = fileItem.getString("utf-8");
					} else {
						if(fileItem.getFieldName().equals("image") && fileItem.getContentType().startsWith("image/")) {
							image = ImageIO.read(fileItem.getInputStream());
						}
						fileItem.delete();
					}
				}
			} catch (FileUploadException | IOException e) {
				APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-400));
				return;
			}
		} else {
			tokenKey = request.getParameter("token");
			apiKey = request.getParameter("apikey");
			projectId = request.getParameter("project");
			name = request.getParameter("name");
			action = request.getParameter("action");
			contrast = request.getParameter("contrast");
			brightness = request.getParameter("brightness");
			mirror_x = request.getParameter("mirror_x");
			mirror_y = request.getParameter("mirror_y");
			angle = request.getParameter("angle");
			x1 = request.getParameter("x1");
			y1 = request.getParameter("y1");
			width = request.getParameter("width");
			height = request.getParameter("height");
			lt_x = request.getParameter("lt_x");
			lt_y = request.getParameter("lt_y");
			rt_x = request.getParameter("rt_x");
			rt_y = request.getParameter("rt_y");
			rb_x = request.getParameter("rb_x");
			rb_y = request.getParameter("rb_y");
			lb_x = request.getParameter("lb_x");
			lb_y = request.getParameter("lb_y");
		}
		if(!APIFilter.filter(apiKey)) {
			APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-1002));
			return;
		}
		if(!TokenFilter.filter(tokenKey)) {
			APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-1003));
			return;
		}
		DatabaseUtils dbUtils = new DatabaseUtils(getServletContext());
		Connection connection = dbUtils.getConnection(false);
		if(connection == null) {
			dbUtils.closeConnection(false);
			APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(500));
			return;
		}
		Token token = AuthFilter.doFilter(connection, response, apiKey, tokenKey);
		if(token == null) {
			dbUtils.closeConnection(false);
			APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-401));
			return;
		}

		if(image != null && image.getType() != BufferedImage.TYPE_3BYTE_BGR) {
			dbUtils.closeConnection(false);
			APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-1200));
			return;
		}
		int thumbnailMaxSize = Integer.parseInt(getServletContext().getInitParameter("project_thumbnail_max_size"));
		if(projectId == null && image != null) {
			/* new */
			Project project = new Project();
			project.setCreated(new Date());
			project.setUser(token.getUser());
			project.setId(KeyUtils.getStringKey());
			project.setTasks(new ImageScanner<BufferedImage>(BufferedImage.class).toJSON());
			project.setRaw(bufferedImageToByteArray(image));
			project.setThumbnail(bufferedImageToByteArray(makeThumbnail(image, thumbnailMaxSize)));
			
			try {
				ProjectDAO.insert(connection, project);
				dbUtils.closeConnection(true);
				JsonObject jsonObject = ResponseCodeUtils.getJSON(200);
				jsonObject.addProperty("image", "data:image/jpeg;base64," + Base64.encodeBase64String(project.getThumbnail()));
				jsonObject.addProperty("project", project.getId());
				APIUtils.writeJSON(response, jsonObject);
			} catch (SQLException e) {
				dbUtils.closeConnection(false);
				APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-500));
			}
		} else if(projectId != null && action != null) {
			Project project = null;
			try {
				project = ProjectDAO.select(connection, projectId, token.getUser().getPhoneNumber());
			} catch (SQLException | ParseException e) { }
			if(project == null) {
				dbUtils.closeConnection(false);
				APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-401));
				return;
			}
			image = byteArrayToBufferedImage(project.getRaw());
			
			/* update */
			if(action.equalsIgnoreCase("reset")) {
				project.setTasks(new ImageScanner<BufferedImage>(BufferedImage.class).toJSON());
				project.setThumbnail(bufferedImageToByteArray(makeThumbnail(image, thumbnailMaxSize)));
				try {
					ProjectDAO.update(connection, project);
					dbUtils.closeConnection(true);
					JsonObject jsonObject = ResponseCodeUtils.getJSON(200);
					jsonObject.addProperty("image", "data:image/jpeg;base64," + Base64.encodeBase64String(project.getThumbnail()));
					APIUtils.writeJSON(response, jsonObject);
				} catch (SQLException e) {
					dbUtils.closeConnection(false);
					APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-500));
				}
			} else if(action.equalsIgnoreCase("try")) {
				ImageScanner<BufferedImage> scanner = new ImageScanner<BufferedImage>(BufferedImage.class, project.getTasks());
				if(contrast != null && brightness != null) {
					try {
						double contrastDouble = Double.parseDouble(contrast);
						double brightnessDouble = Double.parseDouble(brightness);
						if(contrastDouble != 0 || brightnessDouble != 0)
							scanner.addTask(new AdjustmentScanTask(contrastDouble, brightnessDouble));
					} catch (NumberFormatException e) { }
				}
				boolean flip_x = false, flip_y = false;
				if(mirror_x != null && mirror_x.equals("1"))
					flip_x = true;
				if(mirror_y != null && mirror_y.equals("1"))
					flip_y = true;
				if(flip_x || flip_y)
					scanner.addTask(new MirrorScanTask(flip_x, flip_y));
				if(angle != null) {
					try {
						int angleVal = Integer.parseInt(angle);
						scanner.addTask(new RotatingScanTask(0.5, 0.5, angleVal));
					} catch (NumberFormatException e) { }
				}
				BufferedImage doneImage = null;
				try {
					doneImage = scanner.run(image);
				} catch (IllegalArgumentException e) { }
				if(doneImage == null) {
					dbUtils.closeConnection(false);
					APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-400));
					return;
				}
				JsonObject jsonObject = ResponseCodeUtils.getJSON(200);
				jsonObject.addProperty("image", "data:image/jpeg;base64," + Base64.encodeBase64String(bufferedImageToByteArray(makeThumbnail(doneImage, thumbnailMaxSize))));
				dbUtils.closeConnection(false);
				APIUtils.writeJSON(response, jsonObject);
			} else if(action.equalsIgnoreCase("modify")) {
				ImageScanner<BufferedImage> scanner = new ImageScanner<BufferedImage>(BufferedImage.class, project.getTasks());
				if(contrast != null && brightness != null) {
					try {
						double contrastDouble = Double.parseDouble(contrast);
						double brightnessDouble = Double.parseDouble(brightness);
						if(contrastDouble != 0 || brightnessDouble != 0)
							scanner.addTask(new AdjustmentScanTask(contrastDouble, brightnessDouble));
					} catch (NumberFormatException e) { }
				}
				boolean flip_x = false, flip_y = false;
				if(mirror_x != null && mirror_x.equals("1"))
					flip_x = true;
				if(mirror_y != null && mirror_y.equals("1"))
					flip_y = true;
				if(flip_x || flip_y)
					scanner.addTask(new MirrorScanTask(flip_x, flip_y));
				if(angle != null) {
					try {
						int angleVal = Integer.parseInt(angle);
						scanner.addTask(new RotatingScanTask(0.5, 0.5, angleVal));
					} catch (NumberFormatException e) { }
				}
				if(x1 != null && y1 != null && width != null & height != null) {
					try {
						double x1Double = Double.parseDouble(x1);
						double y1Double = Double.parseDouble(y1);
						double widthDouble = Double.parseDouble(width);
						double heightDouble = Double.parseDouble(height);
						if(x1Double != 0 || y1Double !=0 || widthDouble != 1 || heightDouble != 1)
							scanner.addTask(new CroppingScanTask(x1Double, y1Double, widthDouble, heightDouble));
					} catch (NumberFormatException e) { }
				}
				project.setTasks(scanner.toJSON());
				BufferedImage doneImage = null;
				try {
					doneImage = scanner.run(image);
				} catch (IllegalArgumentException e) { }
				if(doneImage == null) {
					dbUtils.closeConnection(false);
					APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-400));
					return;
				}
				project.setThumbnail(bufferedImageToByteArray(makeThumbnail(doneImage, thumbnailMaxSize)));
				try {
					ProjectDAO.update(connection, project);
					dbUtils.closeConnection(true);
					JsonObject jsonObject = ResponseCodeUtils.getJSON(200);
					jsonObject.addProperty("image", "data:image/jpeg;base64," + Base64.encodeBase64String(project.getThumbnail()));
					APIUtils.writeJSON(response, jsonObject);
				} catch (SQLException e) {
					dbUtils.closeConnection(false);
					APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-500));
				}
			} else {
				APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-400));
			}
		} else if(projectId != null && name != null && name.trim().length() > 0) {
			/* commit */
			Project project = null;
			try {
				project = ProjectDAO.select(connection, projectId, token.getUser().getPhoneNumber());
			} catch (SQLException | ParseException e) { }
			if(project == null) {
				dbUtils.closeConnection(false);
				APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-401));
				return;
			}
			image = byteArrayToBufferedImage(project.getRaw());			
			ImageScanner<BufferedImage> scanner = new ImageScanner<BufferedImage>(BufferedImage.class, project.getTasks());	
			if(lt_x != null && lt_y != null && rt_x != null && rt_y != null && rb_x != null && rb_y != null && lb_x != null && lb_y != null) {
				double ltX, ltY, rtX, rtY, rbX, rbY, lbX, lbY;
				try {
					ltX = Double.parseDouble(lt_x);
					ltY = Double.parseDouble(lt_y);
					rtX = Double.parseDouble(rt_x);
					rtY = Double.parseDouble(rt_y);
					rbX = Double.parseDouble(rb_x);
					rbY = Double.parseDouble(rb_y);
					lbX = Double.parseDouble(lb_x);
					lbY = Double.parseDouble(lb_y);
				} catch (NumberFormatException e) {
					dbUtils.closeConnection(false);
					APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-400));
					return;
				}
				scanner.addTask(new CorrectionScanTask(new RelativePoint(ltX, ltY), new RelativePoint(rtX, rtY), new RelativePoint(rbX, rbY), new RelativePoint(lbX, lbY)));
			}
			project.setTasks(scanner.toJSON());
			BufferedImage doneImage = null;
			try {
				doneImage = scanner.run(image);
			} catch (IllegalArgumentException e) { }
			if(doneImage == null) {
				dbUtils.closeConnection(false);
				APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-400));
				return;
			}
			project.setRaw(bufferedImageToByteArray(doneImage));
			project.setThumbnail(bufferedImageToByteArray(makeThumbnail(doneImage, Integer.parseInt(getServletContext().getInitParameter("gallery_thumbnail_max_size")))));
			try {
				Image imageModel = new Image();
				imageModel.setCreated(new Date());
				imageModel.setId(KeyUtils.getStringKey());
				imageModel.setName(name);
				imageModel.setRaw(project.getRaw());
				imageModel.setThumbnail(project.getThumbnail());
				imageModel.setUser(token.getUser());
				ImageDAO.insert(connection, imageModel);
				ProjectDAO.delete(connection, project);
				dbUtils.closeConnection(true);
				JsonObject jsonObject = ResponseCodeUtils.getJSON(200);
				jsonObject.addProperty("image", imageModel.getId());
				APIUtils.writeJSON(response, jsonObject);
			} catch (SQLException e) {
				dbUtils.closeConnection(false);
				APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-500));
			}
		} else {
			dbUtils.closeConnection(false);
			APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-400));
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		APIUtils.writeJSON(response, ResponseCodeUtils.getJSON(-400));
	}

}
