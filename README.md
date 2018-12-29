<p align="center">
	<img src="https://github.com/premacck/facilis/blob/master/app/src/main/ic_launcher-web.png" width="40%">
</p>

# Facilis
[![](https://jitpack.io/v/premacck/facilis.svg)](https://jitpack.io/#premacck/facilis)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Facilis-yellowgreen.svg?style=flat)](https://android-arsenal.com/details/1/7409)

Swipe gesture based navigational library for Android.

<p align="center">
	<a href="https://play.google.com/store/apps/details?id=com.prembros.facilis.sample">
		<img src="https://github.com/steverichey/google-play-badge-svg/blob/master/img/en_get.svg" width="40%">
	</a>
</p>

Watch Demo Video:

[![YouTube demo](https://github.com/premacck/facilis/blob/master/video_thumb.png)](https://www.youtube.com/watch?v=YlNkM7NfZec&t=1s)

## Getting Started

To get this project into your build:

### Gradle

Add it in your root build.gradle at the end of repositories:
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

Add the dependency to your module's build.gradle
```
dependencies {
	implementation 'com.github.premacck:facilis:1.0.1'
}
```

### Maven

```
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>
```
Add the dependency
```
<dependency>
    <groupId>com.github.premacck</groupId>
    <artifactId>facilis</artifactId>
    <version>1.0.0</version>
</dependency>
```

### SBT
Add it in your build.sbt at the end of resolvers:
```
resolvers += "jitpack" at "https://jitpack.io"
```
Add the dependency
```
libraryDependencies += "com.github.premacck" % "facilis" % "1.0.0"
```

### Leiningen
Add it in your project.clj at the end of repositories:
```
:repositories [["jitpack" "https://jitpack.io"]]
```
Add the dependency
```
:dependencies [[com.github.premacck/facilis "1.0.0"]]
```

### Usage
- Refer to the [wiki pages](https://github.com/premacck/facilis/wiki) for step by step implementations

## Built With

* [Picasso](http://square.github.io/picasso/) - Image loading
* [Deck ViewPager](https://github.com/bloderxd/deck) - Customized ViewPager
* [Kotlin](https://kotlinlang.org/) - For less code
* [Anko](https://github.com/Kotlin/anko) - For easier life
* [BlurKit](https://github.com/CameraKit/blurkit-android) - For awesome blurring effects

## Contributing

Please read [CONTRIBUTING.md](https://github.com/premacck/facilis/blob/master/CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags).

## Authors

* **Prem Suman** - *A-Z* - [GitHub](https://github.com/premacck)

See also the list of [contributors](https://github.com/premacck/facilis/contributors) who participated in this project.

## Acknowledgments

* Hat tip to anyone whose code was used
