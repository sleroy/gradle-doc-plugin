<!DOCTYPE html>

<html>

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>$pagetitle$ - Komea: KPI Dashboard for software
	development</title>

<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="http://code.jquery.com/jquery-migrate-1.2.1.min.js"></script>


<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">

<!-- Optional theme -->
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">

<!-- Latest compiled and minified JavaScript -->
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>

<link rel="shortcut icon" href="favicon.ico" />
<script type="text/javascript"
	src="https://apis.google.com/js/plusone.js">
        {lang:'en', parsetags:'explicit'}
    </script>

<link href="styles/thymeleaf.css" rel="stylesheet"
	type="text/css" />
<script type="text/javascript" src="scripts/shCore.js"></script>
<script type="text/javascript" src="scripts/shBrushXml.js"></script>
<script type="text/javascript" src="scripts/shBrushJava.js"></script>
<link href="/styles/shCore.css" rel="stylesheet" type="text/css" />
<link href="/styles/shThemeThymeleaf.css" rel="stylesheet"
	type="text/css" />

<link rel="icon" href="images/icon.png" />
<link rel="stylesheet" href="css/fonts/open-sans.css" />
<link rel="stylesheet" href="css/fonts/source-sans-pro.css" />
<link rel="stylesheet" href="css/normalize.css" />
<link rel="stylesheet" href="css/thymeleaf.css" />
<link rel="stylesheet" href="css/thymeleaf-screen.css" media="screen" />
<link rel="stylesheet" href="css/thymeleaf-print.css" media="print" />
<script src="scripts/prettify.min.js"></script>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script src="scripts/thymeleaf-tutorials.js"></script>
<script type="text/javascript" src="/scripts/thymeleaf-articles.js"></script>

</head>




<body class="tutorial">

	<div id="documentation-wrapper">

		<div id="title-block-wrapper">
			<header id="title-block">
				<img id="doc-banner" class="banner" src="images/header.png"
					alt="Komea logo" />
				<h1 id="doc-title" class="title">$title$</h1>
				<div id="doc-info">
					<div id="doc-version">
						<span class="info-header">Document version:</span>
						@documentVersion@
					</div>
					<div id="project-version">
						<span class="info-header">Project Version:</span> @projectVersion@
					</div>
					<div id="project-website">
						<span class="info-header">Project web site:</span> <a
							href="http://www.tocea.com">http://www.tocea.com</a>
					</div>
				</div>
			</header>
		</div>

		<div id="toc-wrapper">
			<nav id="toc">$toc$</nav>
		</div>

		<div id="content-wrapper">
			<div id="content">$body$</div>
		</div>

	</div>

</body>

</html>
