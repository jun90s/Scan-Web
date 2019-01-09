$(function () {
  var handleError = function (e) {
    if (e.msg != undefined)
      alert(e.msg);
    else
      alert("发生了一个错误，请重试！");
  };
  if ($.cookie("token") != null) {
    $(window).attr('location', "gallery.jsp");
  }
  $(".btn-next").click(function () {
    if ($("#step").val() == 1) {
      $.ajax({
        type: "post",
        url: "login/ready",
        cache: false,
        data: {
          apikey: "epDSMnxvEmGeHVn8QcjJsTW1PIFDFork",
          phonenumber: $("#phonenumber").val()
        },
        dataType: "json",
        success: function (data) {
          if (data.code == 200) {
            $("#phonenumber").attr("type", "hidden");
            $("#code").attr("type", "text");
            $("#prompt").text("请输入您收到的验证码");
            $("#step").val(parseInt($("#step").val()) + 1);
          } else {
            handleError(data);
          }
        },
        error: function () {
          handleError(-1);
        }
      });
    } else {
      $.ajax({
        type: "post",
        url: "login",
        cache: false,
        data: {
          apikey: "epDSMnxvEmGeHVn8QcjJsTW1PIFDFork",
          phonenumber: $("#phonenumber").val(),
          code: $("#code").val()
        },
        dataType: "json",
        success: function (data) {
          if (data.code == 200) {
            $.cookie("phonenumber", data.phonenumber)
            $.cookie("token", data.token);
            $.cookie("admin", data.admin);
            $(window).attr('location', "gallery.jsp");
          } else {
            handleError(data);
          }
        },
        error: function () {
          handleError(-1);
        }
      });
    }
  });
});