<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>invitation</title>
    <script src="${pageContext.request.contextPath}/statics/jquery-3.5.1.js"></script>
    <script>
        function validate() {
            $.ajax({
                url:"${pageContext.request.contextPath}/invitation/checkInvitation",
                data:{"invitationCode":$("#invitationCode").val()},
                success:function (data) {
                    console.log(data);
                    console.log(data.toString());
                }
            });
        }
    </script>
</head>
<body>

<div>
    <span>邀请码:</span> <input type="text" id="invitationCode" >
    <span>提交</span><input type="button" onclick="validate()">
</div>
</body>
</html>
