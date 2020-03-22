Dim soundFart(3)
soundFart(0) = LoadSound("sfx/fart0.mp3")
soundFart(1) = LoadSound("sfx/fart1.mp3")
soundFart(2) = LoadSound("sfx/fart2.mp3")
soundFart(3) = LoadSound("sfx/fart3.mp3")
Global channelFart

Dim radio(9)
;radio(5) = LoadSound("sfx/radio/Wild Horse.mp3")
Global radioChannel
Global radioSelected = 5 ; to which radio station the user is listening

Function PlayRadio()
    If Not ChannelPlaying(radioChannel) Then
        radioChannel = PlaySound(radio(radioSelected))
    EndIf

    If player\vehicle <> 0 Then
        ChannelVolume radioChannel, 0.5
    Else
        ChannelVolume radioChannel, 0.0
    EndIf
End Function