$(document).ready(function(){
        $('select').niceSelect();

        var menuArrow = $('#menuBtn i');
        var menuWrap = $('.menu-wrap');
        var sebmitBtn = $('#submitBtn');
        var mapAreaSetGuide = $('.map-active-mode');
        var mainMap = $('#map');

        $('#menuBtn').click(function(){
            if(menuWrap.hasClass('menu-in')){
                menuWrap.removeClass('menu-in');
                menuWrap.addClass('menu-out');
                menuArrow.removeClass('roll');
            }else if(menuWrap.hasClass('menu-out')){
                menuWrap.removeClass('menu-out');
                menuWrap.addClass('menu-in');
                menuArrow.addClass('roll');
            }
        });
        sebmitBtn.click(function(){
            if(matchMedia("screen and (max-width: 1024px)").matches){
                menuWrap.removeClass('menu-out');
                menuWrap.addClass('menu-in');
                menuArrow.addClass('roll');
            }else{}
        });
        $('.close-btn').click(function(){
                $('.modal').modal('hide');
        });
        // 좌표설정모드(임시)
        $('#btn_area_set').click(function(){
            mainMap.css('z-index',1100).addClass('active');
            mapAreaSetGuide.show();
        });
        mapAreaSetGuide.click(function(){
           mainMap.css('z-index',1).removeClass('active');
            mapAreaSetGuide.hide();
        });
});

function f_click_add_area(){
    $('#addAreaModal').modal('show');
}
function f_click_guideBtn(){
    $('#guideModal').modal('show');
}