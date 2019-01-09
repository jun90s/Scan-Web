$(function () {
  var val_watch;
  var project;
  var corner_free = false;
  var brightness = 0,
    contrast = 0,
    angle = 0,
    x1 = 0,
    y1 = 0,
    x2 = 0,
    y2 = 0,
    mirror_x = false,
    mirror_y = false;
  if ($.cookie("token") == null) {
    $(window).attr("location", "auth.jsp");
  }
  $(".btn-app-download").click(function () {
    alert("即将到来");
  });
  var handleError = function (e) {
    if (e.msg != undefined)
      alert(e.msg);
    else
      alert("发生了一个错误，请重试！");
  };
  $(".btn-logout").click(function () {
    $.ajax({
      type: "post",
      url: "logout",
      cache: false,
      data: {
        apikey: "epDSMnxvEmGeHVn8QcjJsTW1PIFDFork",
        token: $.cookie("token")
      },
      dataType: "json",
      success: function (data) {
        if (data.code != -500) {
          $.cookie("token", null, {
            expires: -1
          });
          $.cookie("admin", null, {
            expires: -1
          });
          $.cookie("phonenumber", null, {
            expires: -1
          });
          $(window).attr("location", "index.jsp");
        } else {
          handleError(data);
        }
      },
      error: function () {
        handleError(-1);
      }
    });
  });
  $(".btn-new").click(function () {
    $("#upload-image").click();
  });
  var update_image = function (src, do_init_corner, callback) {
    var image = new Image();
    image.src = src;
    image.onload = function () {
      $("#canvas-container").css("width", image.width);
      $("#canvas-container").css("height", image.height);
      $("#canvas").attr("width", image.width);
      $("#canvas").attr("height", image.height);
      var init_corner = function () {
        $("#brightness").val(0);
        $("#contrast").val(0);
        $("#angle").val(0);
        $("#mirror_x").prop("checked", false);
        $("#mirror_y").prop("checked", false);
        $("#corner-lt").css("top", $("#canvas")[0].getBoundingClientRect().top);
        $("#corner-lt").css("left", $("#canvas")[0].getBoundingClientRect().left);
        $("#corner-rt").css("top", $("#canvas")[0].getBoundingClientRect().top);
        $("#corner-rt").css("left", $("#canvas")[0].getBoundingClientRect().left + image.width - 15);
        $("#corner-rb").css("top", $("#canvas")[0].getBoundingClientRect().top + image.height - 15);
        $("#corner-rb").css("left", $("#canvas")[0].getBoundingClientRect().left + image.width - 15);
        $("#corner-lb").css("top", $("#canvas")[0].getBoundingClientRect().top + image.height - 15);
        $("#corner-lb").css("left", $("#canvas")[0].getBoundingClientRect().left);
      }
      if (do_init_corner) init_corner();
      $("#work-panel .col-2").css("height", image.height + 30 + "px");
      $("#canvas")[0].getContext("2d").drawImage(image, 0, 0);
      $("#corner-lt").removeClass("hidden");
      $("#corner-lt").draggable({
        containment: "#canvas-container",
        drag: function () {
          if ($("#corner-rb")[0].getBoundingClientRect().left - $("#corner-lt")[0].getBoundingClientRect().left < 30) {
            init_corner();
            return false;
          }
          if ($("#corner-rb")[0].getBoundingClientRect().top - $("#corner-lt")[0].getBoundingClientRect().top < 30) {
            init_corner();
            return false;
          }
          if (!corner_free) {
            $("#corner-rt").css("top", $("#corner-lt").css("top"));
            $("#corner-lb").css("left", $("#corner-lt").css("left"));
          }
        },
      });
      $("#corner-rt").draggable({
        containment: "#canvas-container",
        drag: function () {
          if ($("#corner-rt")[0].getBoundingClientRect().left - $("#corner-lb")[0].getBoundingClientRect().left < 30) {
            init_corner();
            return false;
          }
          if ($("#corner-lb")[0].getBoundingClientRect().top - $("#corner-rt")[0].getBoundingClientRect().top < 30) {
            init_corner();
            return false;
          }
          if (!corner_free) {
            $("#corner-lt").css("top", $("#corner-rt").css("top"));
            $("#corner-rb").css("left", $("#corner-rt").css("left"));
          }
        },
      });
      $("#corner-rb").draggable({
        containment: "#canvas-container",
        drag: function () {
          if ($("#corner-rb")[0].getBoundingClientRect().left - $("#corner-lt")[0].getBoundingClientRect().left < 30) {
            init_corner();
            return false;
          }
          if ($("#corner-rb")[0].getBoundingClientRect().top - $("#corner-lt")[0].getBoundingClientRect().top < 30) {
            init_corner();
            return false;
          }
          if (!corner_free) {
            $("#corner-lb").css("top", $("#corner-rb").css("top"));
            $("#corner-rt").css("left", $("#corner-rb").css("left"));
          }
        },
      });
      $("#corner-lb").draggable({
        containment: "#canvas-container",
        drag: function () {
          if ($("#corner-rt")[0].getBoundingClientRect().left - $("#corner-lb")[0].getBoundingClientRect().left < 30) {
            init_corner();
            return false;
          }
          if ($("#corner-lb")[0].getBoundingClientRect().top - $("#corner-rt")[0].getBoundingClientRect().top < 30) {
            init_corner();
            return false;
          }
          if (!corner_free) {
            $("#corner-rb").css("top", $("#corner-lb").css("top"));
            $("#corner-lt").css("left", $("#corner-lb").css("left"));
          }
        },
      });
    }
    if (callback != undefined) callback();
  };
  var start_val_change_listener = function () {
    brightness = $("#brightness").val();
    contrast = $("#contrast").val();
    angle = $("#angle").val();
    mirror_x = $("#mirror_x").prop("checked");
    mirror_y = $("#mirror_y").prop("checked");
    $("#brightness").removeAttr("disabled");
    $("#contrast").removeAttr("disabled");
    $("#angle").removeAttr("disabled");
    $("#mirror_x").removeAttr("disabled");
    $("#mirror_y").removeAttr("disabled");
    $(".btn-reset").css("pointer-events", "auto");
    $(".btn-modify").css("pointer-events", "auto");
    $(".btn-next").css("pointer-events", "auto");
    $("#corner-lt").css("pointer-events", "auto");
    $("#corner-rt").css("pointer-events", "auto");
    $("#corner-rb").css("pointer-events", "auto");
    $("#corner-lb").css("pointer-events", "auto");
    val_watch = setInterval(function () {
      var change = false;
      if ($("#brightness").val() != brightness) {
        change = true;
        brightness = $("#brightness").val();
      }
      if ($("#contrast").val() != contrast) {
        change = true;
        contrast = $("#contrast").val();
      }
      if ($("#angle").val() != angle) {
        change = true;
        angle = $("#angle").val();
      }
      if ($("#mirror_x").prop("checked") != mirror_x) {
        change = true;
        mirror_x = $("#mirror_x").prop("checked");
      }
      if ($("#mirror_y").prop("checked") != mirror_y) {
        change = true;
        mirror_y = $("#mirror_y").prop("checked");
      }
      if (change) {
        clearInterval(val_watch);
        val_watch = undefined;
        $("#brightness").attr("disabled", "disabled");
        $("#contrast").attr("disabled", "disabled");
        $("#angle").attr("disabled", "disabled");
        $("#mirror_x").attr("disabled", "disabled");
        $("#mirror_y").attr("disabled", "disabled");
        $(".btn-reset").css("pointer-events", "none");
        $(".btn-modify").css("pointer-events", "none");
        $(".btn-next").css("pointer-events", "none");
        $("#corner-lt").css("pointer-events", "none");
        $("#corner-rt").css("pointer-events", "none");
        $("#corner-rb").css("pointer-events", "none");
        $("#corner-lb").css("pointer-events", "none");
        $.ajax({
          type: "post",
          url: "project",
          cache: false,
          data: {
            apikey: "epDSMnxvEmGeHVn8QcjJsTW1PIFDFork",
            token: $.cookie("token"),
            project: project,
            action: "try",
            brightness: brightness / 100.0,
            contrast: contrast / 100.0,
            angle: angle,
            mirror_x: mirror_x ? 1 : 0,
            mirror_y: mirror_y ? 1 : 0
          },
          dataType: "json",
          success: function (data) {
            if (data.code == 200) {
              update_image(data.image, false, function () {
                start_val_change_listener();
              });
            } else {
              handleError(data);
            }
          },
          error: function () {
            handleError(-1);
          }
        });
      }
    }, 1000);
  }
  $("#upload-image").change(function () {
    var form_data = new FormData();
    form_data.append("apikey", "epDSMnxvEmGeHVn8QcjJsTW1PIFDFork");
    form_data.append("token", $.cookie("token"));
    form_data.append("image", $("#upload-image")[0].files[0]);
    var col = $("#ready-panel > .row > .col-1:nth-child(2)");
    col.empty();
    col.append($("<p></p>").attr("id", "prompt").addClass("center").text("请稍等"));
    col.append($("<p></p>").attr("id", "upload-progress").addClass("center").text("正在上传"));
    $.ajax({
      type: "post",
      url: "project",
      cache: false,
      data: form_data,
      contentType: false,
      processData: false,
      dataType: "json",
      success: function (data) {
        if (data.code == 200) {
          project = data.project;
          $("#prompt").text("正在准备工作界面");
          $("#canvas-container").append($("<div></div>").addClass("corner").attr("id", "corner-lt").text("LT"));
          $("#canvas-container").append($("<div></div>").addClass("corner").attr("id", "corner-rt").text("RT"));
          $("#canvas-container").append($("<div></div>").addClass("corner").attr("id", "corner-rb").text("RB"));
          $("#canvas-container").append($("<div></div>").addClass("corner").attr("id", "corner-lb").text("LB"));
          $("#upload-progress").text("上传成功");
          update_image(data.image, true, function () {
            $("#work-panel").show();
            $("#ready-panel").hide();
            $(".btn-reset").click(function () {
              if (val_watch != undefined) clearInterval(val_watch);
              $.ajax({
                type: "post",
                url: "project",
                cache: false,
                data: {
                  apikey: "epDSMnxvEmGeHVn8QcjJsTW1PIFDFork",
                  token: $.cookie("token"),
                  project: project,
                  action: "reset"
                },
                dataType: "json",
                success: function (data) {
                  if (data.code == 200) {
                    update_image(data.image, true, function () {
                      start_val_change_listener();
                    });
                  } else {
                    handleError(data);
                  }
                },
                error: function () {
                  handleError(-1);
                }
              });
            });
            $(".btn-modify").click(function () {
              if (val_watch != undefined) clearInterval(val_watch);
              brightness = $("#brightness").val();
              contrast = $("#contrast").val();
              angle = $("#angle").val();
              mirror_x = $("#mirror_x").prop("checked");
              mirror_y = $("#mirror_y").prop("checked");
              var x1 = ($("#corner-lt")[0].getBoundingClientRect().left < $("#corner-lb")[0].getBoundingClientRect().left ? $("#corner-lt")[0].getBoundingClientRect().left : $("#corner-lb")[0].getBoundingClientRect().left) + 15 - $("#canvas")[0].getBoundingClientRect().left;
              var y1 = $("#corner-lt")[0].getBoundingClientRect().top < $("#corner-rt")[0].getBoundingClientRect().top ? $("#corner-lt")[0].getBoundingClientRect().top : $("#corner-rt")[0].getBoundingClientRect().top + 15 - $("#canvas")[0].getBoundingClientRect().top;
              var x2 = $("#corner-rt")[0].getBoundingClientRect().left > $("#corner-rb")[0].getBoundingClientRect().left ? $("#corner-rt")[0].getBoundingClientRect().left : $("#corner-rb")[0].getBoundingClientRect().left + 15 - $("#canvas")[0].getBoundingClientRect().left;
              var y2 = $("#corner-lb")[0].getBoundingClientRect().top > $("#corner-rb")[0].getBoundingClientRect().top ? $("#corner-lb")[0].getBoundingClientRect().top : $("#corner-rb")[0].getBoundingClientRect().top + 15 - $("#canvas")[0].getBoundingClientRect().top;
              $.ajax({
                type: "post",
                url: "project",
                cache: false,
                data: {
                  apikey: "epDSMnxvEmGeHVn8QcjJsTW1PIFDFork",
                  token: $.cookie("token"),
                  project: project,
                  action: "modify",
                  brightness: brightness / 100.0,
                  contrast: contrast / 100.0,
                  angle: angle,
                  mirror_x: mirror_x ? 1 : 0,
                  mirror_y: mirror_y ? 1 : 0,
                  x1: x1 * 1.0 / $("#canvas")[0].width,
                  y1: y1 * 1.0 / $("#canvas")[0].height,
                  width: (x2 - x1) * 1.0 / $("#canvas")[0].width,
                  height: (y2 - y1) * 1.0 / $("#canvas")[0].height
                },
                dataType: "json",
                success: function (data) {
                  if (data.code == 200) {
                    update_image(data.image, true, function () {
                      corner_free = true;
                      $(".sidebar").empty();
                      $(".sidebar").append($("<p></p>").addClass("col-title").text("保存"));
                      var div_name = $("<div></div>");
                      div_name.append($("<p></p>").text("名称"));
                      div_name.append($("<input>").attr("id", "name").attr("type", "text"));
                      $(".sidebar").append($(div_name));
                      var div_btn = $("<div></div>").addClass("btn-group");
                      div_btn.append($("<a></a>").addClass("btn btn-save").attr("href", "#").text("保存"));
                      $(".sidebar").append(div_btn);
                      start_val_change_listener();
                      $(".btn-save").click(function () {
                        if (val_watch != undefined) clearInterval(val_watch);
                        var x1 = ($("#corner-lt")[0].getBoundingClientRect().left < $("#corner-lb")[0].getBoundingClientRect().left ? $("#corner-lt")[0].getBoundingClientRect().left : $("#corner-lb")[0].getBoundingClientRect().left) + 15 - $("#canvas")[0].getBoundingClientRect().left;
                        var y1 = $("#corner-lt")[0].getBoundingClientRect().top < $("#corner-rt")[0].getBoundingClientRect().top ? $("#corner-lt")[0].getBoundingClientRect().top : $("#corner-rt")[0].getBoundingClientRect().top + 15 - $("#canvas")[0].getBoundingClientRect().top;
                        var x2 = $("#corner-rt")[0].getBoundingClientRect().left > $("#corner-rb")[0].getBoundingClientRect().left ? $("#corner-rt")[0].getBoundingClientRect().left : $("#corner-rb")[0].getBoundingClientRect().left + 15 - $("#canvas")[0].getBoundingClientRect().left;
                        var y2 = $("#corner-lb")[0].getBoundingClientRect().top > $("#corner-rb")[0].getBoundingClientRect().top ? $("#corner-lb")[0].getBoundingClientRect().top : $("#corner-rb")[0].getBoundingClientRect().top + 15 - $("#canvas")[0].getBoundingClientRect().top;
                        $.ajax({
                          type: "post",
                          url: "project",
                          cache: false,
                          data: {
                            apikey: "epDSMnxvEmGeHVn8QcjJsTW1PIFDFork",
                            token: $.cookie("token"),
                            project: project,
                            name: $("#name").val(),
                            lt_x: ($("#corner-lt")[0].getBoundingClientRect().left + 15 - $("#canvas")[0].getBoundingClientRect().left) * 1.0 / $("#canvas")[0].width,
                            lt_y: ($("#corner-lt")[0].getBoundingClientRect().top + 15 - $("#canvas")[0].getBoundingClientRect().top) * 1.0 / $("#canvas")[0].height,
                            rt_x: ($("#corner-rt")[0].getBoundingClientRect().left + 15 - $("#canvas")[0].getBoundingClientRect().left) * 1.0 / $("#canvas")[0].width,
                            rt_y: ($("#corner-rt")[0].getBoundingClientRect().top + 15 - $("#canvas")[0].getBoundingClientRect().top) * 1.0 / $("#canvas")[0].height,
                            rb_x: ($("#corner-rb")[0].getBoundingClientRect().left + 15 - $("#canvas")[0].getBoundingClientRect().left) * 1.0 / $("#canvas")[0].width,
                            rb_y: ($("#corner-rb")[0].getBoundingClientRect().top + 15 - $("#canvas")[0].getBoundingClientRect().top) * 1.0 / $("#canvas")[0].height,
                            lb_x: ($("#corner-lb")[0].getBoundingClientRect().left + 15 - $("#canvas")[0].getBoundingClientRect().left) * 1.0 / $("#canvas")[0].width,
                            lb_y: ($("#corner-lb")[0].getBoundingClientRect().top + 15 - $("#canvas")[0].getBoundingClientRect().top) * 1.0 / $("#canvas")[0].height,
                          },
                          dataType: "json",
                          success: function (data) {
                            if (data.code == 200) {
                              if($.cookie("token") != null) {
                        	    $(window).attr('location', "gallery.jsp");
                        	  }
                            } else {
                              handleError(data);
                            }
                          },
                          error: function () {
                            handleError(-1);
                          }
                        });
                      });
                    });
                  } else {
                    handleError(data);
                  }
                },
                error: function () {
                  handleError(-1);
                }
              });
            });
            start_val_change_listener();
          });
        } else {
          $("#prompt").text("请刷新页面重试");
          $("#upload-progress").text("上传失败");
          handleError(data);
        }
      },
      error: function () {
        $("#prompt").text("请刷新页面重试");
        $("#upload-progress").text("上传失败");
        handleError(-1);
      }
    });
  });
});