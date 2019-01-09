$(function () {
  $.ajax({
    type: "post",
    url: "gallery",
    cache: false,
    data: {
      apikey: "epDSMnxvEmGeHVn8QcjJsTW1PIFDFork",
      token: $.cookie("token")
    },
    dataType: "json",
    success: function (data) {
      if (data.code == 200) {
        if (data.images.length == 0) {
          $(".small-panel").text("没有图像");
        } else {
          $(".container").empty();
          var rows = parseInt((data.images.length - 1) / 3) + 1;
          for (i = 0; i < rows; i++)
            $(".container").append($("<div></div>").addClass("row"));
          for (i = 0; i < data.images.length; i++) {
            item = $("<div></div>").addClass("col-1 gallery-item");
            item.append($("<p></p>").addClass("gallery-item-action").append($("<a></a>").addClass("gallery-item-btn-delete").attr("href", "#").attr("data-id", data.images[i].id).text("删除")));
            item.append($("<div></div>").addClass("gallery-item-img").attr("data-id", data.images[i].id).css("background-image", "url(" + data.images[i].image + ")"));
            item.append($("<p></p>").addClass("gallery-item-title center").text(data.images[i].name));
            $(".row").eq(parseInt(i / 3)).append(item);
          }
          $(".gallery-item-img").click(function () {
            window.open('_blank').location = "gallery?token=" + $.cookie("token") +
              "&apikey=epDSMnxvEmGeHVn8QcjJsTW1PIFDFork&action=get&image=" + $(this).data("id");
          });
          $(".gallery-item-btn-delete").click(function () {
            var image = $(this).data("id");
            $.ajax({
              type: "post",
              url: "gallery",
              cache: false,
              data: {
                apikey: "epDSMnxvEmGeHVn8QcjJsTW1PIFDFork",
                token: $.cookie("token"),
                image: image,
                action: "delete"
              },
              dataType: "json",
              success: function (data) {
                if (data.code == 200) {
                  window.location.reload();
                } else {
                  alert(data.msg);
                }
              },
              error: function () {
                alert("发生了一个错误，请重试！");
              }
            });
          });
        }
      } else {
        $(".small-panel").text(data.msg);
      }
    },
    error: function () {
      $(".small-panel").text("发生了一个错误，请重试！");
    }
  });
});