<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<!doctype html>
<html lang="ko">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
	<style>
		/* 추가적인 스타일링을 위한 CSS */
		body {
			height: 100vh;
			display: flex;
			justify-content: center;
			align-items: center;
		}
		.login-container {
			max-width: 400px;
		}
	</style>
	<title>Smart Factory</title>
	<%
		//////////////////////
		//// ERROR 발생 시 ////
		//////////////////////
		try{
			request.setCharacterEncoding("utf-8");
			String Message = (String) request.getAttribute("__ALERT");
			if(Message!=null && Message.length() > 0){
				out.clear();
				pageContext.pushBody();
				out.print("<script>alert('"+Message+"'); </script>");
			}
		}catch(Exception e){ e.printStackTrace(); }
		////////////////////////
		List<String> dateList = (ArrayList) request.getAttribute("__DATELIST");
	%>
</head>
<body>
<div class="container login-container">
	<form action="/doDownload" method="post">
		<h2 class="text-center mb-4">Smart Factory</h2>
		<div class="mb-3">
			<div class="input-group">
				<input class="form-control rounded-end px-4" type="text" id="p_date" name="p_date" readonly>
			</div>
		</div>
		<button type="submit" class="btn btn-primary btn-block">Download Image</button>
		<button type="button" class="btn btn-outline-primary btn-block" onclick="location.href='/Logout'">Logout</button>
	</form>
</div>
<%--<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>--%>
<%--<script src="https://cdn.jsdelivr.net/npm/popper.js@1.12.9/dist/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>--%>
<%--<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>--%>

<script src="/js/jquery.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.8.18/themes/base/jquery-ui.css">
</body>
<script>
	$(function() {
		// 선택 가능한 날짜 목록
		var selectableDates = [];
		<%for(int i=0; i<dateList.size(); i++){%>
		selectableDates[<%=i%>]='<%=dateList.get(i)%>';
		<%}%>

		$("#p_date").datepicker({
			showOn:"both"
			, buttonImage: "/img/calendar.png"
			,buttonImageOnly: true
			,changeMonth:true
			,changeYear:true
			,dateFormat:"yy-mm-dd"
			,dayNames : ['일요일','월요일','화요일','수요일','목요일','금요일','토요일']
			,dayNamesMin : ['일','월','화','수','목','금','토']
			,monthNamesShort:  [ "1월", "2월", "3월", "4월", "5월", "6월","7월", "8월", "9월", "10월", "11월", "12월" ]
			,showOtherMonths:true
			,beforeShowDay: function(date) {
				var formattedDate = $.datepicker.formatDate("yy-mm-dd", date);
				// 만약 현재 날짜가 selectableDates 배열에 포함되어 있다면 선택 가능하게 함
				if ($.inArray(formattedDate, selectableDates) != -1) {
					return [true];
				}
				return [false];
			}
		});

		$(".ui-datepicker-trigger").css("width","25px");
		$(".ui-datepicker-trigger").css("height","25px");
		$(".ui-datepicker-trigger").css("align-self","center");
		$(".ui-datepicker-trigger").css("opacity","0.5");
		$(".ui-datepicker-trigger").css("display","inline-block");
		$(".ui-datepicker-trigger").css("position","absolute");
		$(".ui-datepicker-trigger").css("right","10px");

		$("#p_date").click( function() {
			$("#ui-datepicker-div").css("z-index","1056");
		} );

		$('#p_date').datepicker('setDate', selectableDates[selectableDates.length-1]); //(-1D:하루전, -1M:한달전, -1Y:일년전), (+1D:하루후, -1M:한달후, -1Y:일년후)

	});

</script>
</html>