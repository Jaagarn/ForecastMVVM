package com.example.forecastmvvm.internal

import java.io.IOException
import java.lang.Exception

class NoConnectivityException: IOException()

class LocationPermissionNotGrantedExcpetion: IOException()

class DateNotFoundException: Exception()