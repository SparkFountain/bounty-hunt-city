Global gameTime = 60*19 ; time in minutes - begin at 10 am
Global nextTick = 0

Const MIN_BRIGHTNESS = 30
Const MAX_BRIGHTNESS = 180
Const LIGHT_FADE_DURATION = 120 ; how long it takes for dust and dawn
Global lightFadeFactor# = Float(MAX_BRIGHTNESS - MIN_BRIGHTNESS) / LIGHT_FADE_DURATION

Function GetTime$()
    Local time$

    ; hours
    local hours = gameTime / 60
    If hours < 10 Then
        time = "0" + hours
    Else
        time = Str(hours)
    EndIf

    ; delimiter
    time = time + ":"

    ; minutes
    Local minutes = gameTime Mod 60
    If minutes < 10 Then
        time = time + "0" + minutes
    Else
        time = time + Str(minutes)
    EndIf

    Return time
End Function

Function DayNightCicle()
    If nextTick < ms Then
        nextTick = ms + 1000
        gameTime = (gameTime + 1) Mod (60*24)

        Local brightness#
        If gameTime >= 60*21 Or gameTime <= 60*6 Then
            brightness = MIN_BRIGHTNESS
            ChannelVolume ambientChannel, 0
        ElseIf gameTime > 60*6 And gameTime <= 60*8 Then
            brightness = MIN_BRIGHTNESS + (gameTime - 60*6)*lightFadeFactor
            ChannelVolume ambientChannel, Float(gameTime - 60*6) / LIGHT_FADE_DURATION
        ElseIf gameTime > 60*8 And gameTime <= 60*19 Then
            brightness = MAX_BRIGHTNESS
            ChannelVolume ambientChannel, 1
        ElseIf gameTime > 60*19 And gameTime <= 60*21 Then
            brightness = MAX_BRIGHTNESS - Float(gameTime - 60*19)*lightFadeFactor
            ChannelVolume ambientChannel, 1.0 - (Float(gameTime - 60*19) / LIGHT_FADE_DURATION)
        EndIf

        AmbientLight brightness, brightness, brightness
        EntityColor map, brightness, brightness, brightness
        EntityColor water, brightness, brightness, brightness
    EndIf
End Function