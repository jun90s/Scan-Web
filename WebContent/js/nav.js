$(function() {
  var nav_list = new Array(
    {url: "#", text: "注销", class: "btn-logout", admin: false},
    {url: "admin.jsp", text: "管理", class: null, admin: true},
    {url: "about.jsp", text: "关于", class: null, admin: false},
    {url: "#", text: "APP", class: "btn-app-download", admin: false},
    {url: "gallery.jsp", text: "图库", class: null, admin: false},
    {url: "new.jsp", text: "新建", class: null, admin: false}
  );
  nav_list.forEach(function(e) {
    var a = $("<a></a>").attr("href", e.url).text(e.text);
    if(e.class != null)
      a.addClass(e.class);
    if(new RegExp("/" + e.url + "$").test(window.location.pathname))
      a.addClass("active");
    if(!e.admin || $.cookie("admin") != null) 
      $("nav > ul").append($("<li></li>").append(a));
  });
});