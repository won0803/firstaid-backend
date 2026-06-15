<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="ko">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <link href="https://cdn.jsdelivr.net/gh/gitbrent/bootstrap4-toggle@3.6.1/css/bootstrap4-toggle.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/assets/css/nice-select.css">
    <link rel="stylesheet" href="/assets/css/style.css">

    <script src="https://kit.fontawesome.com/f48fa93ecc.js" crossorigin="anonymous"></script>
    <script type="text/javascript" src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/gh/gitbrent/bootstrap4-toggle@3.6.1/js/bootstrap4-toggle.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.min.js"></script>
    <script src="/assets/js/jquery.nice-select.min.js"></script>
    <script src="/assets/js/common.js"></script>

    <script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=1b8832b3e104040a5aa7c173d3a67c4b&libraries=services,clusterer,drawing"></script>

    <title>First Aid</title>
    <style>


        .close-btn { background: none; border: none; font-size: 20px; cursor: pointer; }
        #submitBtn {
            background-color: #1abc9c !important;
            color: white !important;
            width: 100%;
            height: 45px;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            font-weight: 600;
            transition: background 0.3s;
        }
        #submitBtn:hover {
            background-color: #16a085 !important;
        }


        /* 카카오 지도 레이어보다 모달이 무조건 위로 오게 */
        .modal { z-index: 9999 !important; }
        .modal-backdrop { z-index: 9998 !important; }

        /* 지도가 활성화되었을 때만 격자를 덮도록 함 */
        #map.active { z-index: 1100 !important; }
        #map { z-index: 1 !important; }
        .total-loader {
            position: absolute;
            top: 0; left: 0; width: 100%; height: 100%;
            background: rgba(255, 255, 255, 0.9); /* 배경을 약간 더 불투명하게 */
            display: none; /* 기본 숨김 */
            flex-direction: column;
            justify-content: center;
            align-items: center;
            z-index: 100;
        }
        .spinner {
            width: 50px; height: 50px;
            border: 5px solid #f3f3f3;
            border-top: 5px solid #2db4b4;
            border-radius: 50%;
            animation: spin 1s linear infinite;
            margin-bottom: 15px;
        }
        @keyframes spin { 0% { transform: rotate(0deg); } 100% { transform: rotate(360deg); } }
        .total-loader p { color: #2db4b4; font-weight: bold; }

        .map-active-mode {
            /* 기존 스타일들... */
            pointer-events: none; /* [핵심] 마우스 이벤트를 무시하고 아래 레이어로 전달함 */

            /* 혹시 클릭 시 드래그(블록 지정)되는 현상도 막고 싶다면 추가 */
            user-select: none;
        }

        /* 에러 메시지 스타일 */
        .error-msg {
            display: none;
            color: #e74c3c;
            font-size: 11px;
            margin-top: 5px;
            font-weight: 500;
        }

        /* 검증 실패 시 입력창 스타일 */
        .is-invalid {
            border: 1px solid #e74c3c !important;
            background-color: #fff8f8 !important;
        }

        /* 지점 추가 버튼 영역 조정 */
        .main-button-area {
            margin-top: 15px;
        }

    </style>
</head>

<body>
<header>
    <h1>
        <span>firstAid</span>
        <a href="/"><img src="/assets/images/logo_color.png" alt="메인 로고"></a>
    </h1>
</header>

<div class="menu-wrap menu-in">
    <button type="button" id="menuBtn" class="menu-btn btn"><i class="fa fa-angle-right roll" aria-hidden="true"></i></button>
    <div class="form-group main-controls flex-box just-between flex-column">
        <div class="scroll-wrap">
            <div class="flex-box just-between flex-column">
                <div class="flex-box flex-column gap-25">
                    <button type="button" class="guid_btn" id="guidBtn" onclick="f_click_guideBtn()">
                        <i class="fa fa-exclamation-circle" aria-hidden="true"></i> 기능 설명서</button>

                    <div class="flex-box flex-column">
                        <label for="addr" class="label-marker">지역</label>
                        <select name="addr" id="addr">
                            <option value="">지역 선택</option>
                            <option value="원주시">원주시</option>
                            <option value="강릉시">강릉시</option>
                            <option value="고성군">고성군</option>
                            <option value="동해시">동해시</option>
                            <option value="삼척시">삼척시</option>
                            <option value="속초시">속초시</option>
                            <option value="양구군">양구군</option>
                            <option value="양양군">양양군</option>
                            <option value="영월군">영월군</option>
                            <option value="인제군">인제군</option>
                            <option value="정선군">정선군</option>
                            <option value="철원군">철원군</option>
                            <option value="춘천시">춘천시</option>
                            <option value="태백시">태백시</option>
                            <option value="평창군">평창군</option>
                            <option value="홍천군">홍천군</option>
                            <option value="화천군">화천군</option>
                            <option value="횡성군">횡성군</option>
                        </select>
                    </div>

                    <div class="flex-box gap-15 mobile-flex">
                        <div class="flex-box flex-column w-100">
                            <label for="startDate" class="label-marker">시작일</label>
                            <input type="date" id="startDate" name="trip-start">
                        </div>
                        <div class="flex-box flex-column w-100">
                            <label for="endDate" class="label-marker">종료일</label>
                            <input type="date" id="endDate" name="trip-end">
                        </div>
                    </div>

                    <hr>

                    <div class="flex-box just-between">
                        <label class="form-check-label label-marker" for="area_toggle">지점통계</label>
                        <input type="checkbox" id="area_toggle" data-toggle="toggle" data-size="xs">
                    </div>
                </div>

                <div class="flex-box gap-15 mobile-flex main-button-area">
                    <input type="button" id="btn_add_area" onclick="f_click_add_area()" value="검증 지점 추가">
                    <input type="button" value="조회" id="submitBtn" class="submit-btn" onclick="doSearch()">

                </div>
            </div>
        </div>
    </div>
</div>

<div id="map" class="container_map">
    <div id="active-mode-banner" class="map-active-mode">좌표 입력 모드입니다. 지도를 클릭해 주세요.</div>
</div>

<footer>
    <small>COPYRIGHT ⓒ 2022 SEEYA INSIGHT. ALL RIGHTS RESERVED.</small>
</footer>

<div id="guideModal" class="modal fade bd-example-modal-lg" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h2 class="modal-title">기능 설명서</h2>
                <button type="button" class="close-btn" data-dismiss="modal"><i class="fa fa-times" aria-hidden="true"></i></button>
            </div>
            <div class="modal-body guide-contents">
                <dl><dt>격자영역 사고빈도 heatmap</dt><dd><ul><li><div class="guide-box">
                    <img src="/assets/images/guide1.png" alt="기능 설명서1">
                </div></li><li>지역, 기간, 지점통계 on/off 설정 후 조회 버튼 클릭</li></ul></dd></dl>

                <dl><dt>격자영역 통계</dt><dd><ul><li><div class="guide-box">
                    <img src="/assets/images/guide2.png" alt="기능 설명서2">
                </div></li><li>지도상의 아무 격자나 클릭 하면 해당 격자의 영역통계 이벤트 발생 </li></ul></dd></dl>

                <dl><dt>지점 추가</dt><dd><ul><li><div class="guide-box">
                    <img src="/assets/images/guide5.png" alt="기능 설명서3">
                </div></li><li>검증 지점 추가 버튼 클릭시 좌표입력 및 지점 추가 가능 </li></ul></dd></dl>

                <dl><dt>검증지점 조회</dt><dd><ul><li><div class="guide-box">
                    <div><img src="/assets/images/guide3.png" alt="기능 설명서4">
                </div></div></li><li>지점통계 on설정 후 조회 버튼 클릭 </li></ul></dd></dl>

                <dl><dt>검증지점 통계</dt><dd><ul><li><div class="guide-box">
                    <img src="/assets/images/guide4.png" alt="기능 설명서4">
                </div></li><li>마커 클릭시 지점 통계 이벤트 발생</li></ul></dd></dl>

            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="squareDataModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-lg" style="max-width: 900px;">
        <div class="modal-content">
            <div class="modal-header" style="background-color: #1abc9c; color: white;">
                <h5 class="modal-title">영역 통계</h5>
                <button type="button" class="close close-btn" data-dismiss="modal" style="color: white;">&times;</button>
            </div>
            <div class="modal-body" style="background-color: #f8f9fa; position: relative; min-height: 400px;">
                <div id="modal-total-loader" class="total-loader">
                    <div class="spinner"></div>
                    <p>통계 데이터를 분석 중입니다...</p>
                </div>

                <div class="stat-section">
                    <h6>개요</h6>
                    <div id="stat_summary"></div>
                </div>

                <div class="stat-section">
                    <h6>증상별 통계</h6>
                    <div style="height: 250px;"><canvas id="symptomChart"></canvas></div>
                </div>

                <div class="stat-section">
                    <h6>장소별 통계</h6>
                    <div style="height: 250px;"><canvas id="locationChart"></canvas></div>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="addAreaModal" class="modal fade bd-example-modal-sm" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <h2 class="modal-title">지점 추가</h2>
                <button type="button" class="close-btn" data-dismiss="modal"><i class="fa fa-times" aria-hidden="true"></i></button>
            </div>
            <div class="modal-body">
                <div class="form-group flex-box flex-column gap-15">
                    <div class="flex-box flex-column">
                        <label for="new_area_name" class="label-marker">지점명</label>
                        <input type="text" id="new_area_name" placeholder="지점명">
                        <span class="error-msg" id="err_name">지점명을 입력해주세요.</span>
                    </div>

                    <div class="flex-box flex-column gap-10">
                        <div class="flex-box gap-15">
                            <div class="flex-box flex-column w-100">
                                <label for="new_area_x" class="label-marker">GIS_X(경도)</label>
                                <input type="text" id="new_area_x" readonly>
                            </div>
                            <div class="flex-box flex-column w-100">
                                <label for="new_area_y" class="label-marker">GIS_Y(위도)</label>
                                <input type="text" id="new_area_y" readonly>
                            </div>
                        </div>
                        <span class="error-msg" id="err_coord">좌표 입력 시작 버튼을 눌러 지도에서 지점을 선택해 주세요.</span>
                        <input type="button" value="좌표입력 시작" id="btn_area_set" class="btn btn-sm btn-info w-100">
                    </div>

                    <hr>

                    <div class="flex-box flex-column">
                        <label for="new_area_radius" class="label-marker">반경</label>
                        <input type="number" id="new_area_radius" placeholder="반경(m)">
                        <span class="error-msg" id="err_radius">분석 반경을 입력해주세요.</span>
                    </div>

                    <div class="flex-box flex-column">
                        <label for="new_area_addr" class="label-marker">시/군/구</label>
                        <input type="text" id="new_area_addr" placeholder="시/군/구">
                        <span class="error-msg" id="err_addr">지역 정보를 입력해주세요.</span>
                    </div>

                    <input type="button" id="btn_new_area" value="저장" class="btn btn-primary">
                </div>
            </div>
        </div>
    </div>
</div>

<div id="pointDetailModal" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-lg" role="document" style="max-width: 900px;">
        <div class="modal-content">
            <div class="modal-header" style="background-color: #1abc9c; color: white;">
                <h5 class="modal-title">지점 통계</h5>
                <button type="button" class="close close-btn" data-dismiss="modal" style="color: white;"><span>&times;</span></button>
            </div>

            <div class="modal-body" style="background-color: #f8f9fa; position: relative; min-height: 500px;">

                <div id="point-modal-loader" class="total-loader" style="display: none; position: absolute; top: 0; left: 0; width: 100%; height: 100%; background: rgba(255, 255, 255, 0.9); flex-direction: column; justify-content: center; align-items: center; z-index: 100; border-radius: 4px;">
                    <div class="spinner"></div>
                    <p style="color: #2db4b4; font-weight: bold; margin-top: 10px;">지점 데이터를 분석 중입니다...</p>
                </div>

                <div class="info-section mb-4" style="background: white; padding: 15px; border-left: 5px solid #1abc9c; border-radius: 4px; box-shadow: 0 1px 3px rgba(0,0,0,0.1);">
                    <h6 class="bg-light p-2" style="font-weight: bold; border-radius: 2px; margin-bottom: 15px;">개요</h6>
                    <div id="point_summary" style="padding: 10px; line-height: 1.8; font-size: 14px;">
                        <div><b>지점명 :</b> <span id="p_modal_name">-</span></div>
                        <div style="margin-bottom: 10px;"><b>출동 횟수 :</b> <span id="p_modal_cnt" style="color:red; font-weight:bold;">0</span> 회</div>
                        <div class="row" style="background: #f9f9f9; padding: 10px; border-radius: 4px;">
                            <div class="col-6" style="border-right: 1px solid #eee;">
                                <b style="color: #555;">[출동시간]</b><br>
                                평균: <span id="p_modal_avg_t">0</span> 분 /
                                최소: <span id="p_modal_min_t">0</span> 분 /
                                최대: <span id="p_modal_max_t">0</span> 분
                            </div>
                            <div class="col-6">
                                <b style="color: #555;">[출동거리]</b><br>
                                평균: <span id="p_modal_avg_d">0</span> km /
                                최소: <span id="p_modal_min_d">0</span> km /
                                최대: <span id="p_modal_max_d">0</span> km
                            </div>
                        </div>
                    </div>
                </div>

                <div class="chart-section" style="background: white; padding: 15px; border-left: 5px solid #1abc9c; border-radius: 4px; box-shadow: 0 1px 3px rgba(0,0,0,0.1);">
                    <h6 class="bg-light p-2" style="font-weight: bold; border-radius: 2px;">일별 평균 출동시간</h6>
                    <div style="height: 200px; margin-bottom: 40px;">
                        <canvas id="pointTimeChart"></canvas>
                    </div>

                    <h6 class="bg-light p-2" style="font-weight: bold; border-radius: 2px;">일별 평균 출동거리</h6>
                    <div style="height: 200px;">
                        <canvas id="pointDistChart"></canvas>
                    </div>
                </div>
            </div>

            <div class="modal-footer" style="border-top: none; background-color: #f8f9fa;">
                <button type="button" class="btn btn-danger btn-sm" onclick="f_delete_point()">지점 삭제</button>
                <button type="button" class="btn btn-secondary btn-sm" data-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
    // --------------------------------------------------------
    // 1. 전역 변수 선언
    // --------------------------------------------------------
    var map;                // 카카오 지도 객체
    var geocoder;           // 좌표 <-> 주소 변환 객체
    var symptomChart = null; // 증상별 차트 객체
    var locationChart = null;// 장소별 차트 객체
    var rectangles = [];     // 지도 위 격자 객체 저장 배열
    var pointMarkers = [];   // 지점 마커 저장 배열
    var pointCircles = [];   // 지점 반경 원 저장 배열
    var isPickingMode = false; // 지도 클릭 위치 선택 모드 여부
    var pointTimeChart = null; //그래프
    var pointDistChart = null;//
    // 마지막으로 실제 조회가 이루어진 날짜를 저장하는 변수
    var lastSearchedStartDate = "";
    var lastSearchedEndDate = "";
    // --------------------------------------------------------
    // 2. 지도 초기화 및 이벤트 설정
    // ------------S--------------------------------------------
    kakao.maps.load(function() {
        var mapContainer = document.getElementById('map');
        var mapOption = {
            center: new kakao.maps.LatLng(37.3422, 127.9202),
            level: 5
        };

        map = new kakao.maps.Map(mapContainer, mapOption);
        geocoder = new kakao.maps.services.Geocoder();


        kakao.maps.event.addListener(map, 'click', function(mouseEvent) {
            if (isPickingMode) {
                var latlng = mouseEvent.latLng;

                // 1. 좌표 값 입력
                $('#new_area_x').val(latlng.getLng().toFixed(6));
                $('#new_area_y').val(latlng.getLat().toFixed(6));

                // [추가] 좌표 입력 시 빨간 테두리 및 에러 메시지 즉시 제거
                $('#new_area_x, #new_area_y').removeClass('is-invalid');
                $('#err_coord').hide();

                // 2. 지도의 권한 및 UI 복구
                isPickingMode = false;
                $('#map').css('z-index', 1).removeClass('active');
                $('#map').css('cursor', '');
                $('.map-active-mode').hide();

                // 3. 지점 마커/원 클릭 기능 복구
                if (typeof pointMarkers !== 'undefined') {
                    pointMarkers.forEach(function(m) { if(m.setClickable) m.setClickable(true); });
                }
                if (typeof pointCircles !== 'undefined') {
                    pointCircles.forEach(function(c) { if(c.setClickable) c.setClickable(true); });
                }

                // 4. 기존 모달 잔상 제거
                $('.modal-backdrop').remove();
                $('body').removeClass('modal-open');

                // 5. 주소 변환 후 모달 오픈
                geocoder.coord2Address(latlng.getLng(), latlng.getLat(), function(result, status) {
                    if (status === kakao.maps.services.Status.OK) {
                        var cityName = result[0].address.region_2depth_name || result[0].address.region_1depth_name;
                        $('#new_area_addr').val(cityName);

                        // [추가] 주소 입력 시 빨간 테두리 및 에러 메시지 즉시 제거
                        $('#new_area_addr').removeClass('is-invalid');
                        $('#err_addr').hide();
                    }

                    // 아주 약간의 지연(100ms)을 주어 지도가 완전히 내려간 뒤 모달이 뜨게 함
                    setTimeout(function() {
                        $('#addAreaModal').modal('show');
                    }, 100);
                });
            }
        });
    });

    // 지점 추가 버튼 클릭 시 호출되는 함수
    function f_click_add_area() {
        // 1. 입력 필드 전체 초기화 (이전 데이터 삭제)
        $('#new_area_name').val('');   // 지점명
        $('#new_area_x').val('');      // 경도(X)
        $('#new_area_y').val('');      // 위도(Y)
        $('#new_area_radius').val(''); // 반경
        $('#new_area_addr').val('');   // 주소

        // 2. [핵심] 지도 상태 및 커서 초기화
        // common.js에서 높여놓은 z-index를 1로 되돌려야 격자가 클릭됩니다.
        isPickingMode = false;
        $('#map').css('cursor', '').css('z-index', 1).removeClass('active');
        $('.map-active-mode').hide(); // 안내 상자도 숨김

        // 3. 지점 마커/원 클릭 기능 복구 (좌표 입력 중 잠갔던 기능 해제)
        if (typeof pointMarkers !== 'undefined') {
            pointMarkers.forEach(function(m) { if(m.setClickable) m.setClickable(true); });
        }
        if (typeof pointCircles !== 'undefined') {
            pointCircles.forEach(function(c) { if(c.setClickable) c.setClickable(true); });
        }

        // 4. 좌표입력 버튼 디자인 초기화
        $('#btn_area_set').val("좌표입력 시작").css('background-color', '').css('color', '');

        // 5. 깨끗해진 상태로 모달창 띄우기
        $('#addAreaModal').modal('show');
    }



    function f_click_guideBtn() { $('#guideModal').modal('show'); }

    // --------------------------------------------------------
    // 3. 데이터 조회 및 격자 그리기
    // --------------------------------------------------------
    function doSearch(isSilent = false) {
        var addr = $('#addr').val();
        var startDate = $('#startDate').val();
        var endDate = $('#endDate').val();
        var isAreaToggleOn = $('#area_toggle').prop('checked');

        // 1. [검증 단계] 모든 유효성 검사를 먼저 수행합니다.
        if (!addr) {
            if (!isSilent) Swal.fire({ icon: 'warning', title: '지역 미선택', text: '조회할 지역을 선택해주세요.' });
            $('#addr').focus();
            return;
        }
        if (!startDate || !endDate) {
            if (!isSilent) Swal.fire({ icon: 'warning', title: '날짜 누락', text: '날짜를 입력해주세요.' });
            return;
        }

        var startNum = parseInt(startDate.replace(/-/g, ''));
        var endNum = parseInt(endDate.replace(/-/g, ''));

        if (startNum > endNum) {
            if (!isSilent) {
                Swal.fire({
                    icon: 'error',
                    title: '날짜 범위 오류',
                    html: '시작일이 종료일보다 늦을 수 없습니다.<br>날짜 범위를 다시 확인해주세요.',
                    confirmButtonColor: '#e74c3c'
                });
            }
            return; // 잘못된 날짜이므로 여기서 중단! 아래의 전역 변수 갱신에 도달하지 못함.
        }

        // ----------------------------------------------------------------------
        // 2. [검증 통과] 모든 체크가 끝난 '이 시점'에서만 마지막 성공 날짜를 업데이트합니다.
        // ----------------------------------------------------------------------
        lastSearchedStartDate = startDate.replace(/-/g, '');
        lastSearchedEndDate = endDate.replace(/-/g, '');

        // 3. 실제 조회 로직 시작
        if (isAreaToggleOn) {
            loadPointData(addr, startDate, endDate);
        }

        $.ajax({
            url: '/ajax/getGridData.do',
            type: 'get',
            data: {
                region: addr,
                startDate: lastSearchedStartDate, // 고정된 값 사용
                endDate: lastSearchedEndDate      // 고정된 값 사용
            },
            success: function (data) {
                if (!data || !data.gridList || data.gridList.length === 0) {
                    if (!isSilent) {
                        Swal.fire({ icon: 'info', title: '조회 결과 없음', text: '데이터가 없습니다.', confirmButtonColor: '#1abc9c' });
                    }
                    if (typeof rectangles !== 'undefined') {
                        rectangles.forEach(r => r.setMap(null));
                        rectangles = [];
                    }
                    return;
                }
                drawGrid(data.gridList);
            },
            error: function () {
                if (!isSilent) {
                    Swal.fire({ icon: 'error', title: '시스템 오류', text: '데이터 조회 중 오류가 발생했습니다.' });
                }
            }
        });
    }
    /**
     * [수정] 스위치 OFF 시 조회 버튼 클릭 없이도 즉시 지점(마커, 원, 라벨) 숨기기
     */
    $(document).on('change', '#area_toggle', function() {
        if (!$(this).prop('checked')) {
            console.log("지점 통계 OFF: 모든 지점 제거");

            // 1. 지점 마커 및 라벨(CustomOverlay) 제거
            if (typeof pointMarkers !== 'undefined' && pointMarkers.length > 0) {
                pointMarkers.forEach(function(m) {
                    m.setMap(null);
                });
                pointMarkers = []; // 배열 초기화
            }

            // 2. 지점 원(Circle) 제거
            if (typeof pointCircles !== 'undefined' && pointCircles.length > 0) {
                pointCircles.forEach(function(c) {
                    c.setMap(null);
                });
                pointCircles = []; // 배열 초기화
            }

            // 3. 클러스터러 초기화 (사용 중일 경우)
            if (typeof clusterer !== 'undefined' && clusterer !== null) {
                clusterer.clear();
            }
        }
    });

  // 격자 설정
    function drawGrid(dataList) {
        // 1. 기존 격자 제거
        rectangles.forEach(function(rect) { rect.setMap(null); });
        rectangles = [];

        // [중요] 조회 시점에 전역 변수가 비어있다면 한 번 더 확실히 세팅
        if (!lastSearchedStartDate) {
            lastSearchedStartDate = $('#startDate').val().replace(/-/g, '');
            lastSearchedEndDate = $('#endDate').val().replace(/-/g, '');
        }

        // 2. 격자 그리기 루프
        dataList.forEach(function(item) {
            if (!item.grid_x || !item.grid_y || Number(item.grid_x) == 0 || Number(item.grid_y) == 0) return;

            var count = Number(item.cnt);
            var color = '';
            var opacity = 0.2;

            if (count >= 10) {
                color = '#ff4757';
                opacity = (count >= 20) ? 0.8 : 0.6;
            } else if (count >= 5) {
                color = '#ffa502';
                opacity = (count >= 7) ? 0.6 : 0.4;
            } else {
                color = '#35ef23';
                opacity = (count >= 3) ? 0.4 : 0.2;
            }

            var bounds = new kakao.maps.LatLngBounds(
                new kakao.maps.LatLng(item.grid_y, item.grid_x),
                new kakao.maps.LatLng(Number(item.grid_y) + 0.000996, Number(item.grid_x) + 0.001018)
            );

            var rectangle = new kakao.maps.Rectangle({
                bounds: bounds,
                strokeWeight: 1,
                strokeColor: '#ffffff',
                strokeOpacity: 0.1,
                fillColor: color,
                fillOpacity: opacity,
                zIndex: 10
            });

            rectangle.setMap(map);
            rectangles.push(rectangle);

            // 격자 클릭 이벤트
            kakao.maps.event.addListener(rectangle, 'click', function() {
                if (isPickingMode) return;
                $('#map').css('z-index', 1).removeClass('active');
                $('.map-active-mode').hide();

                // 상세 모달 오픈 함수 호출 (item 데이터 전달)
                f_open_square_modal(item);
            });
        });

        // 3. 지도 중심 이동 로직 (Geocoder)
        if (rectangles.length > 0) {
            var addr = $('#addr').val();
            if (addr && geocoder) {
                geocoder.addressSearch(addr, function(result, status) {
                    if (status === kakao.maps.services.Status.OK) {
                        var coords = new kakao.maps.LatLng(result[0].y, result[0].x);
                        map.setCenter(coords);
                        map.setLevel(7);
                    } else {
                        moveToFirstValidData(dataList);
                    }
                });
            } else {
                moveToFirstValidData(dataList);
            }
        }
    }

    /**
     * 주소 검색에 실패하거나 주소가 없을 경우
     * 데이터 리스트에서 유효한 좌표를 찾아 지도를 이동시키는 보조 함수
     */
    function moveToFirstValidData(dataList) {
        var firstValid = dataList.find(d => Number(d.grid_x) != 0);
        if (firstValid) {
            map.setCenter(new kakao.maps.LatLng(firstValid.grid_y, firstValid.grid_x));
            map.setLevel(5); // 개별 사고 지점으로 이동할 때는 더 가깝게(5레벨) 설정
        }
    }


     // * 역할: 격자(영역) 클릭 시 상세 통계 모달을 열고, 클릭한 위치의 좌표/주소/기간 등 기본 정보를 표시합니다.

    function f_open_square_modal(item) {
        // 1. [로딩 시작]
        $('#modal-total-loader').css('display', 'flex');

        // 2. [초기화] 차트 및 캔버스 리셋
        if (symptomChart) { symptomChart.destroy(); symptomChart = null; }
        if (locationChart) { locationChart.destroy(); locationChart = null; }
        $('#symptomChart').replaceWith('<canvas id="symptomChart"></canvas>');
        $('#locationChart').replaceWith('<canvas id="locationChart"></canvas>');

        // --------------------------------------------------------
        // 3. 모달 표시 및 기본 정보(좌표, 기간) 텍스트 초기화
        // --------------------------------------------------------
        $('#squareDataModal').modal('show');

        // ★ 수정: 실시간 $('#startDate').val() 대신, 전역 변수를 사용하여 날짜를 표시합니다.
        // 날짜 형식이 YYYYMMDD라면 가독성을 위해 하이픈을 다시 넣어주는 처리를 추가했습니다.
        var sDate = lastSearchedStartDate ?
            lastSearchedStartDate.replace(/(\d{4})(\d{2})(\d{2})/, '$1-$2-$3') : "-";
        var eDate = lastSearchedEndDate ?
            lastSearchedEndDate.replace(/(\d{4})(\d{2})(\d{2})/, '$1-$2-$3') : "-";

        var lat = Number(item.grid_y).toFixed(5);
        var lng = Number(item.grid_x).toFixed(5);
        // 모달 상단 요약 영역(Summary)에 표시될 HTML 구성
        var html = '<div style="font-size: 14px; line-height: 2.2; padding: 10px;">';
        html += '<b>좌표:</b> ' + lat + ' / ' + lng + ' <br>';
        html += '<b>주소:</b> <span id="dynamic_addr" > </span> <br>'; // 주소는 하단 geocoder에서 후행 입력
        html += '<b>기간:</b> ' + sDate + ' ~ ' + eDate + ' <br>';
        html += '<b>출동횟수:</b> <span id="modal_total_cnt" style="color:red; font-weight:bold;">데이터 분석중...</span>';
        html += '</div>';

        $('#stat_summary').html(html);

        // --------------------------------------------------------
        // 4. [주소 변환] 좌표를 기반으로 실제 행정 주소 가져오기
        // --------------------------------------------------------
        if (geocoder) {
            // 카카오 맵 Geocoder 서비스 사용 (좌표 -> 주소 변환)
            geocoder.coord2Address(Number(item.grid_x), Number(item.grid_y), function(result, status) {
                if (status === kakao.maps.services.Status.OK) {
                    // 도로명 주소가 있으면 도로명으로, 없으면 지번 주소로 선택
                    var addr = !!result[0].road_address ? result[0].road_address.address_name : result[0].address.address_name;

                    // 위에서 비워두었던 #dynamic_addr 스팬(span) 태그에 주소 삽입
                    $('#dynamic_addr').text(addr).css("color", "#000");
                }
            });
        }

        // 4. 서버 데이터 호출 (격자 상세 통계 정보 요청)
        $.ajax({
            url: '/ajax/getGridDetail.do',
            type: 'POST',
            data: {
                grid_x: item.grid_x, // 선택한 격자의 X 좌표
                grid_y: item.grid_y, // 선택한 격자의 Y 좌표
                startDate: lastSearchedStartDate,
                endDate: lastSearchedEndDate
            },
            success: function(res) {
                var total = 0; // 격자 내 총 출동 횟수를 합산할 변수
                var sList = res.symptomList || []; // 서버에서 전달받은 증상 리스트
                var lList = res.locationList || []; // 서버에서 전달받은 장소 리스트

                // --------------------------------------------------------
                // [증상 데이터 통합 로직] : NaN 및 기타 항목 정제 및 합산
                // --------------------------------------------------------
                var symptomGroup = sList.reduce(function(acc, d) {
                    var label = d.LABEL;
                    // 라벨이 없거나, 'nan'이거나, '기타'라는 단어가 포함되면 '기타통증'으로 통합
                    if (!label || label === 'nan' || label.indexOf('기타') > -1) {
                        label = '기타통증';
                    }
                    if (!acc[label]) acc[label] = 0; // 해당 라벨의 키값이 없으면 초기화
                    acc[label] += Number(d.CNT);    // 출동 횟수 합산
                    total += Number(d.CNT);         // 격자 전체 총합 누적
                    return acc;
                }, {});

                // 객체 형태를 차트 라이브러리용 배열 형태로 변환
                var sData = Object.keys(symptomGroup).map(function(key) {
                    return { LABEL: key, CNT: symptomGroup[key] };
                });

                // 데이터 정렬: 횟수 많은 순(내림차순), 단 '기타통증'은 무조건 맨 뒤로 배치
                sData.sort(function(a, b) {
                    if (a.LABEL === "기타통증") return 1;
                    if (b.LABEL === "기타통증") return -1;
                    return b.CNT - a.CNT;
                });

                // --------------------------------------------------------
                // [장소 데이터 통합 로직] : NaN 및 기타 항목 정제 및 합산
                // --------------------------------------------------------
                var locationGroup = lList.reduce(function(acc, d) {
                    var label = d.LABEL;
                    // 라벨이 없거나, 'nan'이거나, '기타'라는 단어가 포함되면 '기타'로 통합
                    if (!label || label === 'nan' || label.indexOf('기타') > -1) {
                        label = '기타';
                    }
                    if (!acc[label]) acc[label] = 0;
                    acc[label] += Number(d.CNT);
                    return acc;
                }, {});

                // 객체 형태를 차트 라이브러리용 배열 형태로 변환
                var lData = Object.keys(locationGroup).map(function(key) {
                    return { LABEL: key, CNT: locationGroup[key] };
                });

                // 데이터 정렬: 횟수 많은 순(내림차순), 단 '기타'는 무조건 맨 뒤로 배치
                lData.sort(function(a, b) {
                    if (a.LABEL === "기타") return 1;
                    if (b.LABEL === "기타") return -1;
                    return b.CNT - a.CNT;
                });

                // --------------------------------------------------------
                // 5. 화면 업데이트 및 차트 생성
                // --------------------------------------------------------
                // 모달 화면에 최종 합산된 총 출동 횟수 표시
                $('#modal_total_cnt').text(total + "회");

                // 가공된 데이터를 바탕으로 막대 그래프 생성 (drawChart 함수 호출)
                symptomChart = drawChart('symptomChart', sData, symptomChart, '증상별', 10);
                locationChart = drawChart('locationChart', lData, locationChart, '장소별', 10);

                // 6. [로딩 종료]
                // 차트 렌더링이 완료된 후, 사용자에게 부드럽게 화면을 보여주기 위해 0.3초 뒤 로딩 레이어 제거
                setTimeout(function() {
                    $('#modal-total-loader').fadeOut(300);
                }, 300);
            },
            error: function() {
                // 통신 실패 시에도 로딩 레이어는 숨겨서 모달 조작이 가능하도록 처리
                $('#modal-total-loader').hide();
                alert("상세 데이터를 가져오는데 실패했습니다.");
            }
        });
    }

    // --------------------------------------------------------
    // 5. 차트 그리기 함수
    // --------------------------------------------------------

    /**
     * 역할: 데이터를 바탕으로 막대 차트(Bar Chart)를 생성하며, 데이터 규모에 따라 Y축 눈금 간격을 동적으로 조절합니다.
     */
    function drawChart(canvasId, data, chartInstance, label) {
        // 1. 차트를 렌더링할 컨텍스트(Context) 확보
        var ctx = document.getElementById(canvasId).getContext('2d');

        // 2. [초기화] 기존 차트 객체가 존재하면 파괴하여 메모리 누수 및 잔상 현상을 방지
        if (chartInstance) chartInstance.destroy();

        // 3. [데이터 분석] 데이터셋 중 가장 큰 값(최대값)을 찾아 눈금 기준점으로 활용
        var maxVal = Math.max.apply(Math, data.map(function(d) { return d.CNT; }));

        // 4. [동적 눈금 로직] 차트 종류와 데이터 규모에 따라 눈금 간격(stepSize)을 다르게 설정
        var dynamicStepSize;

        if (label === '장소별') {
            // 장소별 차트: 데이터 규모가 상대적으로 작으므로 100을 기준으로 간격 설정
            // 100 이상이면 50단위로 넓게, 그 외에는 10단위로 촘촘하게 표시
            dynamicStepSize = (maxVal >= 100) ? 50 : 10;
        } else {
            // 증상별 차트 (기본): 데이터 규모가 크므로 세 단계로 나누어 설정
            if (maxVal >= 500) {
                dynamicStepSize = 100; // 500 이상이면 100단위
            } else if (maxVal >= 200) {
                dynamicStepSize = 50;  // 200 이상이면 50단위
            } else {
                dynamicStepSize = 10;  // 그 외에는 10단위
            }
        }

        // 5. Chart.js 객체 생성 및 반환
        return new Chart(ctx, {
            type: 'bar', // 막대 그래프
            data: {
                // X축 라벨: 데이터 배열의 LABEL 필드 추출
                labels: data.map(function(d) { return d.LABEL; }),
                datasets: [{
                    label: label,
                    // 실제 수치: 데이터 배열의 CNT 필드 추출
                    data: data.map(function(d) { return d.CNT; }),
                    // 막대 색상: 상위 데이터일수록 붉은색, 하위일수록 민트색 계열로 배치
                    backgroundColor: [
                        'rgba(243,3,23,0.7)', 'rgb(253,108,108)', 'rgb(253,99,46)', 'rgb(255,216,7)',
                        'rgba(122,255,41,0.7)','rgba(144,255,138,0.7)', 'rgba(79,255,112,0.73)',
                        'rgb(149,255,124)','rgb(149,255,124)','rgb(149,255,124)','rgb(149,255,124)',
                        'rgb(0,255,217)','rgb(0,255,217)','rgba(41,255,216,0.7)','rgba(41,255,216,0.7)',
                        'rgba(41,255,216,0.7)','rgba(41,255,216,0.7)','rgba(41,255,216,0.7)','rgba(41,255,216,0.7)',
                    ],
                    borderColor: '#fff', // 막대 테두리 색상
                    borderWidth: 1       // 테두리 두께
                }]
            },
            options: {
                responsive: true,           // 컨테이너 크기에 맞춰 자동 조절
                maintainAspectRatio: false, // 고정 비율 해제 (부모 높이에 맞춤)
                legend: { display: false }, // 범례 숨김 (막대 색상으로 구분 가능하므로)
                scales: {
                    yAxes: [{
                        ticks: {
                            beginAtZero: true,         // Y축이 항상 0부터 시작하도록 설정
                            stepSize: dynamicStepSize, // 앞서 계산된 동적 눈금 간격 적용 (가독성 핵심)
                            fontSize: 11,
                            fontColor: '#999',
                            // 숫자가 클 경우 읽기 편하도록 천단위 콤마(,) 표시
                            callback: function(value) {
                                return value.toLocaleString();
                            }
                        },
                        gridLines: {
                            drawBorder: false,          // 차트 왼쪽 세로 테두리 축선 숨김
                            color: 'rgba(0, 0, 0, 0.05)' // 가로 그리드 선을 아주 연하게 표시
                        }
                    }],
                    xAxes: [{
                        ticks: {
                            autoSkip: true,    // 라벨이 너무 많으면 자동으로 건너뛰어 겹침 방지
                            maxTicksLimit: 15, // X축에 표시될 최대 라벨 개수 제한
                            fontSize: 10,
                            maxRotation: 45,   // 긴 라벨 대비 45도 기울임 고정
                            minRotation: 45
                        },
                        gridLines: {
                            display: false     // 막대 사이의 세로 격자선 제거 (깔끔한 디자인)
                        }
                    }]
                }
            }
        });
    }


   // 6. UI 모드 제어 수정


    $('#btn_area_set').on('click', function() {
        isPickingMode = true;

        // 1. 기존 마커와 원들이 지도 클릭을 방해하지 않도록 클릭 차단 설정
        if (typeof pointMarkers !== 'undefined') {
            pointMarkers.forEach(function(m) { if(m.setClickable) m.setClickable(false); });
        }
        if (typeof pointCircles !== 'undefined') {
            pointCircles.forEach(function(c) { if(c.setClickable) c.setClickable(false); });
        }

        // 2. 현재 모달 닫기
        $('#addAreaModal').modal('hide');

        // 3. 커서 변경
        $('#map').css('cursor', 'crosshair');

        console.log("좌표 선택 모드: 기존 지점 클릭 방지 활성화");
    });

    // --------------------------------------------------------
    // 7. 지점 관련 로직 (저장 및 조회)
    // --------------------------------------------------------
    $('#btn_new_area').on('click', function() {
        // 1. 값 추출
        var name = $('#new_area_name').val().trim();
        var x = $('#new_area_x').val();
        var y = $('#new_area_y').val();
        var radius = $('#new_area_radius').val();
        var addr = $('#new_area_addr').val().trim();

        // 2. 에러 초기화
        $('.error-msg').hide();
        $('#addAreaModal input[type="text"], #addAreaModal input[type="number"]').removeClass('is-invalid');

        // 3. 검증 실행
        var isValid = true;

        if (!name) {
            $('#err_name').show();
            $('#new_area_name').addClass('is-invalid');
            isValid = false;
        }
        if (!x || !y) {
            $('#err_coord').show();
            $('#new_area_x, #new_area_y').addClass('is-invalid');
            isValid = false;
        }
        if (!radius) {
            $('#err_radius').show();
            $('#new_area_radius').addClass('is-invalid');
            isValid = false;
        }
        if (!addr) {
            $('#err_addr').show();
            $('#new_area_addr').addClass('is-invalid');
            isValid = false;
        }

        // 검증 실패 시 중단
        if (!isValid) return;

        // 4. 저장 처리
        $.ajax({
            url: '/ajax/savePoint.do',
            type: 'POST',
            data: { NAME: name, GIS_X: x, GIS_Y: y, RADIUS: radius, LOCATION: addr },
            success: function(res) {
                // 성공 시에는 깔끔하게 SweetAlert2 사용 (창 뒤로 안숨음)
                Swal.fire({
                    icon: 'success',
                    title: '저장 완료',
                    // text 대신 html 속성을 사용하고 <br> 태그를 넣습니다.
                    html: '지점이 성공적으로 등록되었습니다.<br>지점을 보려면 지점통계 ON으로 바꾸고 조회하세요.',
                    confirmButtonColor: '#1abc9c',
                    confirmButtonText: '확인'
                });

                $('#addAreaModal').modal('hide');
                $('#new_area_name, #new_area_x, #new_area_y, #new_area_radius, #new_area_addr').val('');

                if($('#area_toggle').prop('checked')) {
                    doSearch(true);
                }
            },
            error: function(xhr) {
                Swal.fire('오류', '저장에 실패했습니다.', 'error');
            }
        });
    });

    // 추가 팁: 입력 시 에러 메시지 자동 사라짐 로직
    $('#addAreaModal input').on('input change', function() {
        $(this).removeClass('is-invalid');
        $(this).parent().find('.error-msg').hide();
    });


     // * 지점 데이터를 서버에서 불러와 지도에 표시하는 함수

    function loadPointData(region, start, end) {
        // 1. [기존 요소 제거] 지도에 표시된 기존 마커와 반경 원을 모두 삭제하여 초기화
        pointMarkers.forEach(function(m) { m.setMap(null); });
        pointCircles.forEach(function(c) { c.setMap(null); });
        pointMarkers = [];
        pointCircles = [];

        // 2. [서버 통신] 해당 지역 및 기간에 해당하는 지점 리스트 요청
        $.ajax({
            url: '/ajax/getPointList.do',
            type: 'get',
            data: {
                region: region,
                startDate: start.replace(/-/g, ''), // 날짜 포맷 기호(-) 제거
                endDate: end.replace(/-/g, '')
            },
            success: function(res) {
                var list = res.pointList || []; // 서버에서 받은 지점 목록 데이터

                // 3. [반복문] 지점별로 지도 요소(마커, 원, 라벨) 생성
                list.forEach(function(p) {
                    // 좌표 객체 생성 (위도 Y, 경도 X)
                    var pos = new kakao.maps.LatLng(p.GIS_Y, p.GIS_X);

                    // --- 마커 생성 및 지도 표시 ---
                    var marker = new kakao.maps.Marker({
                        position: pos,
                        map: map
                    });
                    pointMarkers.push(marker); // 추후 삭제를 위해 배열에 보관

                    // [이벤트] 마커 클릭 시 상세 통계 모달창을 여는 함수 호출
                    kakao.maps.event.addListener(marker, 'click', function () {
                        f_open_point_modal(p);
                    });

                    // --- 지점별 분석 반경(원) 표시 ---
                    var circle = new kakao.maps.Circle({
                        center: pos,            // 원의 중심 좌표
                        radius: Number(p.RADIUS), // 설정된 반경(m)
                        strokeWeight: 2,        // 선 두께
                        strokeColor: '#1abc9c', // 선 색상 (민트)
                        strokeOpacity: 0.8,     // 선 투명도
                        fillColor: '#1abc9c',   // 채우기 색상
                        fillOpacity: 0.2        // 채우기 투명도
                    });
                    circle.setMap(map);
                    pointCircles.push(circle); // 추후 삭제를 위해 배열에 보관

                    // --- 지점명 라벨(CustomOverlay) 생성 ---
                    // HTML 문자열로 라벨 디자인 정의
                    var content = '<div style="padding: 5px 10px; background: white; border: 1px solid #ccc; ' +
                        'box-shadow: 2px 2px 5px rgba(0,0,0,0.2); font-size: 13px; font-weight: bold; ' +
                        'color: #333; border-radius: 3px; white-space: nowrap;">' +
                        p.NAME + '</div>';

                    var labelOverlay = new kakao.maps.CustomOverlay({
                        position: pos,   // 표시 위치
                        content: content, // 표시할 HTML 내용
                        yAnchor: 2.3     // 마커의 위치보다 위쪽에 표시되도록 높이 조절
                    });

                    // 라벨 지도에 표시 및 배열 보관
                    labelOverlay.setMap(map);
                    pointMarkers.push(labelOverlay);
                });
            }
        });
    }



     // * 역할: 특정 지점의 일별 출동 시간 및 거리 추이를 가져와 선형 그래프(Line Chart)로 시각화합니다.
    function f_load_point_daily_charts(p) {
        // [중요] 만약 첫 조회 전이라 변수가 비어있다면 현재 입력창 값이라도 할당 (예외 방지)
        if(!lastSearchedStartDate) {
            lastSearchedStartDate = $('#startDate').val().replace(/-/g, '');
            lastSearchedEndDate = $('#endDate').val().replace(/-/g, '');
        }

        $.ajax({
            url: '/ajax/getPointDailyStats.do',
            type: 'POST',
            data: {
                GIS_X: p.GIS_X,
                GIS_Y: p.GIS_Y,
                RADIUS: p.RADIUS,
                // 수정: $('#startDate').val()을 버리고 '마지막 조회 날짜' 변수를 사용합니다.
                startDate: lastSearchedStartDate,
                endDate: lastSearchedEndDate
            },
            success: function(dataList) {
                // 1. 이상치(100km 이상) 제외 필터링 로직 포함 가공
                var filteredLabels = [];
                var filteredTimeData = [];
                var filteredDistData = [];

                dataList.forEach(function(d) {
                    if (d.AVG_DIST < 100) {
                        filteredLabels.push(d.LABEL);
                        filteredTimeData.push(d.AVG_TIME);
                        filteredDistData.push(d.AVG_DIST);
                    }
                });

                // 2. 차트 생성 (필터링된 데이터 사용)
                pointTimeChart = drawLineChart('pointTimeChart', filteredLabels, filteredTimeData, '평균 출동시간(분)', pointTimeChart);
                pointDistChart = drawLineChart('pointDistChart', filteredLabels, filteredDistData, '평균 출동거리(km)', pointDistChart);

                setTimeout(function() {
                    $('#point-modal-loader').fadeOut(300);
                }, 300);
            },
            error: function(xhr, status, error) {
                $('#point-modal-loader').hide();
                console.error("지점 차트 데이터 조회 실패:", error);
            }
        });
    }

     // * 역할: 특정 지점(마커) 클릭 시 상세 모달을 열고, 해당 지점의 요약 통계(횟수, 시간, 거리)를 로드합니다.

    function f_open_point_modal(p) {
        // 전역 변수에 현재 선택된 지점의 일련번호를 저장 (삭제 작업 등에서 참조)
        selectedPointSeq = p.SEQ;

        // --------------------------------------------------------
        // 1. [초기화] 이전 지점 데이터의 잔상 제거 및 캔버스 리셋
        // --------------------------------------------------------
        // 모달 내 텍스트 수치들을 로딩 상태 또는 0으로 초기화
        $('#p_modal_name').text("데이터 분석 중...");
        $('#p_modal_cnt').text('0');
        $('#p_modal_avg_t').text('0');
        $('#p_modal_min_t').text('0');
        $('#p_modal_max_t').text('0');
        $('#p_modal_avg_d').text('0');
        $('#p_modal_min_d').text('0');
        $('#p_modal_max_d').text('0');

        // 기존에 생성된 차트 객체가 있다면 파괴 (메모리 누수 방지 및 새 차트 생성 준비)
        if (pointTimeChart) {
            pointTimeChart.destroy();
            pointTimeChart = null;
        }
        if (pointDistChart) {
            pointDistChart.destroy();
            pointDistChart = null;
        }

        // Chart.js 버그(이전 차트 데이터 툴팁이 남는 현상 등)를 완벽히 차단하기 위해 캔버스 태그를 새로 교체
        $('#pointTimeChart').replaceWith('<canvas id="pointTimeChart"></canvas>');
        $('#pointDistChart').replaceWith('<canvas id="pointDistChart"></canvas>');

        // --------------------------------------------------------
        // 2. [로딩 시작] 지점 전용 로딩 레이어 표시
        // --------------------------------------------------------
        $('#point-modal-loader').css('display', 'flex');

        // 3. 부트스트랩 모달 띄우기
        $('#pointDetailModal').modal('show');

        // --------------------------------------------------------
        // 4. [서버 통신] 지점 상세 통계 데이터 가져오기 (AJAX)
        // --------------------------------------------------------
        $.ajax({
            url: '/ajax/getPointDetailStats.do',
            type: 'POST',
            data: {
                GIS_X: p.GIS_X,           // 지점 경도
                GIS_Y: p.GIS_Y,           // 지점 위도
                RADIUS: p.RADIUS,         // 분석 반경
                // 검색 필터의 날짜에서 하이픈(-)을 제거하여 서버 포맷에 맞춤
                startDate: lastSearchedStartDate,
                endDate: lastSearchedEndDate
            },
            success: function (res) {
                // 서버로부터 받아온 데이터로 모달의 텍스트 수치 업데이트
                $('#p_modal_name').text(p.NAME); // 로딩 문구를 실제 지점명으로 교체
                $('#p_modal_cnt').text(res.CNT || 0);         // 총 출동 횟수
                $('#p_modal_avg_t').text(res.AVG_TIME || 0);  // 평균 출동 시간
                $('#p_modal_min_t').text(res.MIN_TIME || 0);  // 최소 출동 시간
                $('#p_modal_max_t').text(res.MAX_TIME || 0);  // 최대 출동 시간
                $('#p_modal_avg_d').text(res.AVG_DIST || 0);  // 평균 출동 거리
                $('#p_modal_min_d').text(res.MIN_DIST || 0);  // 최소 출동 거리
                $('#p_modal_max_d').text(res.MAX_DIST || 0);  // 최대 출동 거리

                // 5. [연쇄 호출] 하단 영역의 일별 추이 그래프 로드 함수 호출
                f_load_point_daily_charts(p);
            },
            error: function (xhr, status, error) {
                console.error("지점 상세 통계 조회 실패:", error);
                // 에러 발생 시 사용자가 모달을 닫거나 조작할 수 있도록 로딩 레이어 숨김
                $('#point-modal-loader').hide();
                alert("상세 통계를 불러오는 중 오류가 발생했습니다.");
            }
        });
    }



     // 역할: 현재 선택되어 모달에 표시 중인 지점을 데이터베이스에서 영구 삭제합니다.

    function f_delete_point() {
        // 1. [SweetAlert2] 세련된 삭제 확인창 표시
        Swal.fire({
            title: '정말 삭제하시겠습니까?',
            text: "삭제된 데이터는 복구할 수 없습니다.",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: '삭제',
            target: document.getElementById('pointDetailModal'),

        }).then((result) => {
            if (result.isConfirmed) {
                // 2. [서버 통신] AJAX 요청
                $.ajax({
                    url: '/ajax/deletePoint.do',
                    type: 'POST',
                    data: { SEQ: selectedPointSeq },
                    success: function(res) {
                        if(res > 0) {
                            // 3. [성공 알림] 줄바꿈을 적용한 성공 메시지
                            Swal.fire({
                                icon: 'success',
                                title: '삭제 완료',
                                html: '지점이 성공적으로 삭제되었습니다.',
                                timer: 2000,
                                showConfirmButton: false
                            });

                            // 4. UI 업데이트
                            $('#pointDetailModal').modal('hide');
                            doSearch(true); // 지도 갱신
                        } else {
                            Swal.fire('실패', '삭제된 데이터가 없거나 실패했습니다.', 'error');
                        }
                    },
                    error: function() {
                        Swal.fire('오류', '서버 통신 중 오류가 발생했습니다.', 'error');
                    }
                });
            }
        });
    }
               // 지점 통계용 선형 차트 생성 함수
    function drawLineChart(canvasId, labels, data, labelName, chartInstance) {
        var ctx = document.getElementById(canvasId).getContext('2d');

        // 1. [객체 초기화] 기존 차트 파괴
        if (chartInstance) chartInstance.destroy();

        // --- [핵심 유지] 이상치(Outlier) 대응을 위한 변수 계산 ---
        var realMax = Math.max.apply(null, data);
        var isDistance = (labelName.indexOf('거리') !== -1);

        // 거리일 경우 max를 20으로 강제 고정, 아닐 경우 데이터에 맞춰 자동 조절
        var yMax = isDistance ? 20 : (realMax > 100 ? 100 : undefined);
        var yStepSize = isDistance ? 5 : 10;

        // 2. 새로운 선형 차트 생성 및 반환
        return new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: labelName,
                    data: data,
                    // --- [디자인] 두 번째 코드의 민트 스타일 적용 ---
                    borderColor: 'rgba(45, 180, 180, 1)',
                    backgroundColor: 'rgba(45, 180, 180, 0.1)',
                    borderWidth: 2,
                    fill: true,
                    tension: 0.4,
                    pointRadius: 1,
                    pointHoverRadius: 5,
                    pointBackgroundColor: 'rgba(45, 180, 180, 1)'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                legend: {
                    display: true,
                    position: 'top',
                    labels: { boxWidth: 10, fontSize: 11, usePointStyle: true }
                },
                scales: {
                    yAxes: [{
                        ticks: {
                            beginAtZero: true,
                            // --- [핵심 유지] Y축 범위 및 단위 설정 ---
                            max: yMax,
                            stepSize: yStepSize,
                            fontSize: 10,
                            fontColor: '#999',
                            padding: 10,
                            callback: function(value) {
                                return value.toLocaleString() + (isDistance ? ' km' : '');
                            }
                        },
                        gridLines: {
                            drawBorder: false,
                            color: 'rgba(0, 0, 0, 0.05)',
                            zeroLineColor: 'rgba(0, 0, 0, 0.05)'
                        }
                    }],
                    xAxes: [{
                        ticks: {
                            fontSize: 10,
                            fontColor: '#999',
                            autoSkip: true,
                            maxTicksLimit: 12,
                            maxRotation: 0,
                            minRotation: 0
                        },
                        gridLines: {
                            display: false
                        }
                    }]
                },
                // --- [디자인] 두 번째 코드의 세련된 흰색 툴팁 스타일 ---
                tooltips: {
                    mode: 'index',
                    intersect: false,
                    backgroundColor: 'rgba(255, 255, 255, 0.9)',
                    titleFontColor: '#333',
                    bodyFontColor: '#666',
                    borderColor: 'rgba(45, 180, 180, 0.3)',
                    borderWidth: 1,
                    displayColors: false,
                    callbacks: {
                        // 툴팁에서도 km/분 단위를 보여주도록 설정
                        label: function(tooltipItem) {
                            return labelName + ": " + tooltipItem.yLabel.toLocaleString() + (isDistance ? " km" : " 분");
                        }
                    }
                }
            }
        });
    }
</script>
</body>
</html>