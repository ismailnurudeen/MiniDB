# MiniDB
An Android Library that extends the capability of shared preferences and adding security

## Setup
-To use the library in your projects,follow the steps below;

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.ElNuru247:MiniDB:1.0'
	}