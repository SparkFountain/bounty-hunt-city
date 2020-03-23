Global settingsFile
Global screen.T_Screen
Global version$

Type T_Screen
    Field width, height, depth, mode
    Field frameRate
    Field vsync
End Type

Function LoadSettings()
    ; TODO: implement JSON reading
End Function

Function LoadDefaultSettings()
    screen.T_Screen = new T_Screen
    screen\width = 1280
    screen\height = 720
    screen\depth = 32
    screen\mode = SCREEN_MODE_WINDOWED_FIX
    screen\frameRate = 60
    screen\vsync = 1

    version = "0.0.1"
End Function