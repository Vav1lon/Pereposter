<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name='layout' content='main'/>
    <title><g:message code="registration.title"/></title>
    <style type='text/css' media='screen'>

    #login {
        margin: 15px 0px;
        padding: 0px;
        text-align: center;
    }

    #login .inner {
        width: 500px;
        padding-bottom: 6px;
        margin: 60px auto;
        text-align: left;
        border: 1px solid #aab;
        background-color: #f0f0fa;
        -moz-box-shadow: 2px 2px 2px #eee;
        -webkit-box-shadow: 2px 2px 2px #eee;
        -khtml-box-shadow: 2px 2px 2px #eee;
        box-shadow: 2px 2px 2px #eee;
    }

    #login .inner .fheader {
        padding: 18px 26px 14px 26px;
        background-color: #f7f7ff;
        margin: 0px 0 14px 0;
        color: #2e3741;
        font-size: 18px;
        font-weight: bold;
    }

    #login .inner .cssform p {
        clear: left;
        margin: 0;
        padding: 4px 0 3px 0;
        padding-left: 105px;
        margin-bottom: 20px;
        height: 1%;
    }

    #login .inner .cssform input[type='text'] {
        width: 180px;
    }

    #login .inner .cssform label {
        font-weight: bold;
        float: left;
        text-align: right;
        margin-left: -105px;
        width: 150px;
        padding-top: 3px;
        padding-right: 50px;
    }

    #login #submit {
        margin-left: 15px;
    }

    #login .inner .login_message {
        padding: 6px 25px 20px 25px;
        color: #c33;
    }

    #login .inner .text_ {
        width: 180px;
    }

    #login .inner .chk {
        height: 12px;
    }
    </style>
</head>

<body>
<div id='login'>
    <div class='inner'>
        <div class='fheader'><g:message code="registration.headFormTitle"/></div>

        <g:if test='${flash.message}'>
            <div class='login_message'>${flash.message}</div>
        </g:if>

        <form action='./registration' method='POST' id='loginForm' class='cssform' autocomplete='on'>
            <p>
                <label for='username'><g:message code="registration.login"/>:</label>
                <input type='text' class='text_' name='login' id='username'/>
            </p>

            <p>
                <label for='password'><g:message code="registration.password"/>:</label>
                <input type='password' class='text_' name='password' id='password'/>
            </p>

            <p>
                <label for='passwordRepeat'><g:message code="registration.repeatpassword"/>:</label>
                <input type='password' class='text_' name='passwordRepeat' id='passwordRepeat'/>
            </p>

            <p>
            <center><input type='submit' id="submit" value='${message(code: "registration.submit")}'/></center>
        </p>
        </form>
    </div>
</div>
</body>
</html>