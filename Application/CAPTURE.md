<!DOCTYPE html>
<html lang="en">

<link href="/bootstrap-3.3.7-dist/css/bootstrap.css" rel="stylesheet">
<script src="/javascripts/jquery-3.2.1.min.js"></script>

<script src="/bootstrap-3.3.7-dist/js/bootstrap.js"></script>

<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">

    <title>오나게</title>
    <link href="https://maxcdn.bootstrapcdn.com/bootswatch/3.3.7/paper/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-awusxf8AUojygHf2+joICySzB780jVvQaVCAt1clU3QsyAitLGul28Qxb2r1e5g+"
          crossorigin="anonymous">

    <!--font-->
    <!--<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"-->
    <!--rel="stylesheet"-->
    <!--integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN"-->
    <!--crossorigin="anonymous">-->

    <!--<link-->
    <!--href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"-->
    <!--rel="stylesheet"-->
    <!--integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"-->
    <!--crossorigin="anonymous">-->
</head>

<style>
    .sidebar {
        border-radius: 20px;
        position: fixed;
        width: 265px;
        height: 55%;
        margin-left: 50%;
        background: mediumpurple;
        overflow-x: hidden;
        overflow-y: auto;
    }

    .sidebar-brand {
        color: white;
        font-size: 150%;
    }

    .sidebar-nav {
        width: 400px;
        margin: 0;
        padding: 0;
        list-style: none;
    }

    .sidebar-nav li {
        border-bottom: 1px solid white;
        text-indent: 1.5em;
        line-height: 2.8em;
    }

    .sidebar-nav li a {
        display: block;
        text-decoration: none;
        font-size: 160%;
        color: white;
        padding-top: 15px;
    }

    .sidebar-nav li a:hover {
        background: #ccccff;
    }
</style>
<body>
<br>
<div class="text-center">
        <span style=" font: normal bold 10em/1em 'Nanum Gothic', serif ; color: indigo;">
            오 ~ 나의 게시판
        </span>
    <br>
    <!--<span style=" font: normal bold 2em/1em Helvetica, serif ; color: darkred;">-->
    <!--#####-->
    <!--</span>-->
</div>
<br>
<div class="container" style="padding-top: 10px;">
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <div class="navbar-header">
                <!--menu on the right-side-->
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                        data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="/">OhNaGe</a>
            </div>

            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav">
                    <li class="active">
                        <a href="/">홈으로</a>
                    </li>
                    <li>
                        <a href="/board/list/1">게시판</a>
                    </li>

                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">
                            HOT-게시판 (연령별/성별) <span class="caret"></span></a>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="/board/M10list/1">10대남 HOT-게시판</a></li>
                            <li><a href="#">20대남 HOT-게시판</a></li>
                            <li><a href="#">30대남 HOT-게시판</a></li>
                            <li><a href="#">40대남 HOT-게시판</a></li>
                            <li><a href="#">50대남 HOT-게시판</a></li>
                            <li class="divider"></li>
                            <li><a href="#">10대여 HOT-게시판</a></li>
                            <li><a href="#">20대여 HOT-게시판</a></li>
                            <li><a href="#">30대여 HOT-게시판</a></li>
                            <li><a href="#">40대여 HOT-게시판</a></li>
                            <li><a href="#">50대여 HOT-게시판</a></li>
                        </ul>
                    </li>
                </ul>
                <form class="navbar-form navbar-left" role="search" method="post" action="/board/list/search/1">
                    <div class="form-group">
                        <input type="text" name="keyword" class="form-control" placeholder="검색어">
                    </div>
                    <button type="submit" class="btn btn-default">검색</button>
                </form>
                <ul class="nav navbar-nav navbar-right">
                    <% if(isLogin){ %>
                    <li><a href="/account/logout" onclick="return confirm('로그아웃 하시겠습니까?')">로그아웃</a></li>
                    <% }else{ %>
                    <li><a href="/account/signup">회원가입</a></li>
                    <li><a href="/account/login">로그인</a></li>
                    <% } %>
                </ul>
            </div>
        </div>
    </nav>

    <div>
        <br><br><br><br><br>
        <div class="sidebar">
            <ul class="sidebar-nav">
                <li class="sidebar-brand">
                    <div class="row">
                        <div class="col-md-4">인기 글</div>
                        <div class="col-md-4">조회 수</div>
                    </div>
                </li>
                <%
                for(var i = 0; i < 5; i++)
                {
                %>
                <li>
                    <a>
                        <div class="row">
                            <div class="col-md-5">sefdf</div>
                            <div class="col-md-5">asefsd</div>
                        </div>
                    </a></li>
                <%
                }
                %>
            </ul>
        </div>
        <br><br><br><br>
    </div>
