$(function () {
  if ($.cookie("admin") == null) {
    $(window).attr('location', "auth.jsp");
  }
  $(".btn-ban").click(function () {
    $.ajax({
      type: "post",
      url: "manage",
      cache: false,
      data: {
        apikey: "epDSMnxvEmGeHVn8QcjJsTW1PIFDFork",
        token: $.cookie("token"),
        phonenumber: $("#phonenumber").val(),
        action: "ban"
      },
      dataType: "json",
      success: function (data) {
        alert(data.msg);
      },
      error: function () {
        alert("发生了一个错误，请重试！");
      }
    });
  });
  $(".btn-unban").click(function () {
    $.ajax({
      type: "post",
      url: "manage",
      cache: false,
      data: {
        apikey: "epDSMnxvEmGeHVn8QcjJsTW1PIFDFork",
        token: $.cookie("token"),
        phonenumber: $("#phonenumber").val(),
        action: "unban"
      },
      dataType: "json",
      success: function (data) {
        alert(data.msg);
      },
      error: function () {
        alert("发生了一个错误，请重试！");
      }
    });
  });
});