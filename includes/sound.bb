Dim soundAmbient(1)
soundAmbient(0) = LoadSound("sfx/ambient1.mp3") : LoopSound soundAmbient(0)
soundAmbient(1) = LoadSound("sfx/ambient2.mp3") : LoopSound soundAmbient(1)

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

Dim soundWalk(3)
soundWalk(0) = LoadSound("sfx/step1.mp3")
soundWalk(1) = LoadSound("sfx/step2.mp3")
soundWalk(2) = LoadSound("sfx/step3.mp3")
soundWalk(3) = LoadSound("sfx/step4.mp3")
Global walkChannel

Dim soundExplosion(0)
soundExplosion(0) = LoadSound("sfx/vehicles/explode.mp3")

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