<%@ page import="com.fasterxml.jackson.databind.JsonNode" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.LocalDate" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
	<style>
		/* 추가적인 스타일링을 위한 CSS */
		body {
			max-height: 100vh;
			display: flex;
			justify-content: center;
			align-items: center;
			margin-top: 50px;
			margin-bottom: 50px;
		}
		.login-container {
			max-width: 1400px;
		}
		/* width */
		::-webkit-scrollbar {
			width: 10px;
		}

		/* Track */
		::-webkit-scrollbar-track {
			background: #f1f1f1;
		}

		/* Handle */
		::-webkit-scrollbar-thumb {
			background: #888;
		}

		/* Handle on hover */
		::-webkit-scrollbar-thumb:hover {
			background: #555;
		}
	</style>
	<title>Client Versions</title>
</head>
<body>
<div class="container login-container pt-9 pb-9">
	<h2>Update Client</h2>
	<table class="table text-center mb-5">
		<thead class="thead-primary">
		<tr>
			<form id="updateForm" name="updateForm" action="/doUpdateClient" method="post" enctype="multipart/form-data" onsubmit="return false;">
				<th style="width: 10%"> <input type="text" class="form-control" id="version" name="version" placeholder="Version"></th>
				<th style="width: 60%"><input type="text" class="form-control" id="changes" name="changes" placeholder="Changes"></th>
				<th style="width: 20%"><input class="form-control" type="file" id="formFile" name="formFile" accept=".zip"></th>
				<th style="width: 10%"><button type="button" class="btn btn-primary btn-block" onclick="javascript:doUpdate()">Update</button></th>
			</form>
		</tr>
		</thead>
	</table>
	<h2>Current Version</h2>
	<table class="table text-center mb-5">
		<thead class="thead-dark">
		<tr>
			<th style="width: 10%">Version</th>
			<th style="width: 20%">Release Date</th>
			<th style="width: 60%">Changes</th>
			<th style="width: 10%">Download</th>
		</tr>
		</thead>
		<tbody style="max-height: 100px;overflow: scroll;">
		<%
			// updateHistory 정보를 가져옵니다.
			JsonNode currentVersion = (JsonNode) request.getAttribute("currentVersion");
			if (currentVersion.get("version").asText().length()>0) {
		%>
		<tr>
			<td><%= currentVersion.get("version").asText() %></td>
			<td><%= currentVersion.get("releaseDate").asText() %></td>
			<td><%= currentVersion.get("changes").asText() %></td>
			<td>
				<a href="<%= currentVersion.get("downloadUrl").asText() %>">
					<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-download" viewBox="0 0 16 16">
						<path d="M.5 9.9a.5.5 0 0 1 .5.5v2.5a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1v-2.5a.5.5 0 0 1 1 0v2.5a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2v-2.5a.5.5 0 0 1 .5-.5"/>
						<path d="M7.646 11.854a.5.5 0 0 0 .708 0l3-3a.5.5 0 0 0-.708-.708L8.5 10.293V1.5a.5.5 0 0 0-1 0v8.793L5.354 8.146a.5.5 0 1 0-.708.708z"/>
					</svg>
				</a>
			</td>
		</tr>
		<%
			} else {
				out.print("<tr><td colspan=\"4\">No Data.</td></tr>");
			}
		%>
		</tbody>
	</table>

	<h2>Update History</h2>
	<div class="table-responsive" style="height: 300px; overflow-y: auto;">
		<table class="table text-center mb-5">
			<thead class="thead-light">
			<tr>
				<th style="width: 10%">Version</th>
				<th style="width: 20%">Release Date</th>
				<th style="width: 60%">Changes</th>
				<th style="width: 10%">Download</th>
			</tr>
			</thead>
			<tbody>
			<%
				// updateHistory 정보를 가져옵니다.
				JsonNode updateHistory = (JsonNode) request.getAttribute("updateHistory");
				if (!updateHistory.isEmpty()) {
					for (JsonNode update : updateHistory) {
			%>
			<tr>
				<td><%= update.get("version").asText() %></td>
				<td><%= update.get("releaseDate").asText() %></td>
				<td><%= update.get("changes").asText() %></td>
				<td>
					<a href="<%=update.get("downloadUrl").asText() %>">
						<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-download" viewBox="0 0 16 16">
							<path d="M.5 9.9a.5.5 0 0 1 .5.5v2.5a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1v-2.5a.5.5 0 0 1 1 0v2.5a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2v-2.5a.5.5 0 0 1 .5-.5"/>
							<path d="M7.646 11.854a.5.5 0 0 0 .708 0l3-3a.5.5 0 0 0-.708-.708L8.5 10.293V1.5a.5.5 0 0 0-1 0v8.793L5.354 8.146a.5.5 0 1 0-.708.708z"/>
						</svg>
					</a>
				</td>
			</tr>
			<%
					}
				} else {
					out.print("<tr><td colspan=\"4\">No Data.</td></tr>");
				}
			%>
			</tbody>
		</table>
	</div>
	<button type="button" class="btn btn-outline-primary btn-block col-lg-3 m-auto" onclick="location.href='/Logout'">Logout</button>
</div>
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
<script type="text/javascript">
	function doUpdate() {
		var form = document.updateForm;

		if (!form.version.value){
			alert("Enter version.");
			form.version.focus();
			return;
		}

		if (!form.changes.value){
			alert("Enter changes.");
			form.changes.focus();
			return;
		}

		if (!form.formFile.value){
			alert("Upload client file.");
			form.formFile.focus();
			return;
		}

		if (form.version.value <= '<%=currentVersion.get("version").asText()%>'){
			alert("Check version.");
			form.version.focus();
			return;
		}

		form.submit();
	}
</script>
</body>
</html>