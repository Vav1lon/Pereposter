$(document).ready(function () {

    $('.item').each(function () {
        $('#' + $(this).id).click(function () {
            $(this).addClass("active");
        });
    });

});