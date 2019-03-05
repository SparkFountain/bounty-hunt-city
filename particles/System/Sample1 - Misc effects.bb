Include "SampleFunctions.bb"

InitGraphics()
AmbientLight 70, 60, 50

;Camera
Cam = CreateCamera()
CameraFogMode Cam, True
CameraFogRange Cam, 0, 1000

;Light
RotateEntity CreateLight(), 45, 45, 0

;Particles
InitParticles(Cam)

;Scene
CreateScene()

While Not KeyHit(1)
	UpdateProgram(Cam, "Press keys 1-7 to see the effects.")
Wend
FreeParticles()
End

Global channel, last_emitted
Function UpdateSample()
For i = 1 To 7
	If KeyHit(i + 1) Then
		StopChannel channel
		PositionEntity emitter, 0, 0, 10
		If i = 6 Then PositionEntity emitter, Rnd(-9, 9), Rnd(-5, 5), 10
		If last_emitted <> 1 Then SetEmitter(emitter, t[i], True)
		channel = PlaySound(ts[i])
		If cnt_emitters = 0 Then last_emitted = 0 Else last_emitted = i
	EndIf
Next
End Function

Global emitter, t[10], ts[10]
Function CreateScene()
;Emitter
emitter = CreatePivot()

;Template1 - 1000 Particles
t[1] = CreateTemplate()
SetTemplateEmitterBlend(t[1], 1)
SetTemplateInterval(t[1], 1)
SetTemplateParticlesPerInterval(t[1], 10)
SetTemplateMaxParticles(t[1], 1000)
SetTemplateEmitterLifeTime(t[1], 300)
SetTemplateParticleLifeTime(t[1], 100, 100)
SetTemplateTexture(t[1], "Media\Spark.jpg", 2, 3)
SetTemplateVelocity(t[1], -.1, .1, .15, .25, .1, .1)
SetTemplateGravity(t[1], .0075)
SetTemplateAlphaVel(t[1], True)
SetTemplateSize(t[1], .2, .2)
SetTemplateColors(t[1], $FFFFFF, $FFBB00)

;Template2 - Smoke
t[2] = CreateTemplate()
SetTemplateEmitterBlend(t[2], 1)
SetTemplateInterval(t[2], 1)
SetTemplateEmitterLifeTime(t[2], 100)
SetTemplateParticleLifeTime(t[2], 30, 45)
SetTemplateTexture(t[2], "Media\Smoke.png", 2, 1)
SetTemplateOffset(t[2], -.3, .3, -.3, .3, -.3, .3)
SetTemplateVelocity(t[2], -.04, .04, .1, .2, -.04, .04)
SetTemplateAlphaVel(t[2], True)
SetTemplateSize(t[2], 3, 3, .5, 1.5)
SetTemplateSizeVel(t[2], .01, 1.01)

;Template3 - Sparks
t[3] = CreateTemplate()
SetTemplateEmitterBlend(t[3], 3)
SetTemplateInterval(t[3], 1)
SetTemplateParticlesPerInterval(t[3], 5)
SetTemplateEmitterLifeTime(t[3], 165)
SetTemplateParticleLifeTime(t[3], 20, 30)
SetTemplateTexture(t[3], "Media\Spark.png", 2, 3)
SetTemplateOffset(t[3], -.1, .1, -.1, .1, -.1, .1)
SetTemplateVelocity(t[3], -.3, .3, -.3, .3, .2, .2)
SetTemplateAlignToFall(t[3], True, 45)
SetTemplateGravity(t[3], .02)
SetTemplateAlphaVel(t[3], True)
SetTemplateSize(t[3], .5, 1, .7, 1)
SetTemplateColors(t[3], $0000FF, $6565FF)
SetTemplateFloor(t[3], -5.5, .5)
;Spark center
t0 = CreateTemplate()
SetTemplateEmitterBlend(t0, 3)
SetTemplateInterval(t0, 1)
SetTemplateEmitterLifeTime(t0, 140)
SetTemplateParticleLifeTime(t0, 30, 40)
SetTemplateTexture(t0, "Media\Spark.png", 3)
SetTemplateVelocity(t0, -.001, .001, -.001, .001, -.001, .001)
SetTemplateRotation(t0, -20, 20)
SetTemplateSize(t0, .4, 4)
SetTemplateAlphaVel(t0, True)
SetTemplateSubTemplate(t[3], t0)

;Template4 - Fire
t[4] = CreateTemplate()
SetTemplateEmitterBlend(t[4], 1)
SetTemplateInterval(t[4], 1)
SetTemplateEmitterLifeTime(t[4], 180)
SetTemplateParticleLifeTime(t[4], 20, 30)
SetTemplateTexture(t[4], "Media\Fire.jpg", 3, 3)
SetTemplateOffset(t[4], -.3, .3, -.3, .3, -.3, .3)
SetTemplateVelocity(t[4], -.04, .04, .1, .2, -.04, .04)
SetTemplateRotation(t[4], -3, 3)
SetTemplateAlphaVel(t[4], True)
SetTemplateSize(t[4], 2, 2)

;Template5 - Explosion1
t[5] = CreateTemplate()
SetTemplateEmitterBlend(t[5], 1)
SetTemplateInterval(t[5], 1)
SetTemplateParticlesPerInterval(t[5], 8)
SetTemplateEmitterLifeTime(t[5], 1)
SetTemplateParticleLifeTime(t[5], 60, 75)
SetTemplateTexture(t[5], "Media\Stone.png", 4)
SetTemplateOffset(t[5], -.4, .4, -.4, .4, -.4, .4)
SetTemplateVelocity(t[5], -.2, .2, -.1, .2, -.2, .2)
SetTemplateRotation(t[5], -3, 3)
SetTemplateGravity(t[5], .02)
SetTemplateSize(t[5], .4, .4, .5, 2)
SetTemplateFloor(t[5], -5, .45)
;Exploion fog
t0 = CreateTemplate()
SetTemplateEmitterBlend(t0, 1)
SetTemplateInterval(t0, 1)
SetTemplateEmitterLifeTime(t0, 8)
SetTemplateParticleLifeTime(t0, 30, 40)
SetTemplateTexture(t0, "Media\Smoke.png", 2, 1)
SetTemplateOffset(t0, -1.7, 1.7, -1.7, 1.7, -1.7, 1.7)
SetTemplateVelocity(t0, -.01, .01, .01, .02, -.01, .01)
SetTemplateSize(t0, 2, 2, .6, 1.3)
SetTemplateSizeVel(t0, .02, 1)
SetTemplateAlpha(t0, .38)
SetTemplateAlphaVel(t0, True)
SetTemplateSubTemplate(t[5], t0)

;Template6 - Exploion2
t[6] = CreateTemplate()
SetTemplateEmitterBlend(t[6], 3)
SetTemplateInterval(t[6], 1)
SetTemplateParticlesPerInterval(t[6], 3)
SetTemplateEmitterLifeTime(t[6], 8)
SetTemplateParticleLifeTime(t[6], 50, 60)
SetTemplateTexture(t[6], "Media\Smoke.png", 3, 3)
SetTemplateOffset(t[6], -.7, .7, -.7, .7, .3, 1.7)
SetTemplateVelocity(t[6], -.03, .03, -.03, .03, -.03, .03)
SetTemplateSize(t[6], 2.2, 2.2)
SetTemplateAlphaVel(t[6], True)
SetTemplateColors(t[6], $FFFF00, $FFAA44)
;Explosion sparks
t1 = CreateTemplate()
SetTemplateEmitterBlend(t1, 3)
SetTemplateInterval(t1, 1)
SetTemplateParticlesPerInterval(t1, 3)
SetTemplateEmitterLifeTime(t1, 8)
SetTemplateParticleLifeTime(t1, 50, 65)
SetTemplateTexture(t1, "Media\Spark.jpg", 3, 3)
SetTemplateOffset(t1, -.4, .4, -.4, .4, -.4, .4)
SetTemplateVelocity(t1, -.1, .1, -.1, .1, -.1, .1)
SetTemplateGravity(t1, .002)
SetTemplateAlignToFall(t1, True, 45)
SetTemplateSize(t1, .15, .08)
SetTemplateAlphaVel(t1, True)
SetTemplateColors(t1, $FFFF33, $FFFF33)
SetTemplateSubTemplate(t[6], t1)
;Exploion fog
t1 = CreateTemplate()
SetTemplateEmitterBlend(t1, 1)
SetTemplateInterval(t1, 1)
SetTemplateEmitterLifeTime(t1, 8)
SetTemplateParticleLifeTime(t1, 80, 110)
SetTemplateTexture(t1, "Media\Smoke.png", 2, 1)
SetTemplateOffset(t1, -1.2, 1.2, -1.2, 1.2, -1.2, 1.2)
SetTemplateVelocity(t1, -.01, .01, .01, .02, -.01, .01)
SetTemplateSize(t1, 2, 2, .7, 1.2)
SetTemplateAlpha(t1, .5)
SetTemplateAlphaVel(t1, True)
SetTemplateSubTemplate(t[6], t1)

;Template7 - Rain
t[7] = CreateTemplate()
SetTemplateEmitterBlend(t[7], 3)
SetTemplateInterval(t[7], 1)
SetTemplateParticlesPerInterval(t[7], 10)
SetTemplateEmitterLifeTime(t[7], 230)
SetTemplateParticleLifeTime(t[7], 20, 20)
SetTemplateTexture(t[7], "Media\Rain.png", 3)
SetTemplateOffset(t[7], -10, 10, 4.5, 5.5, -5, 5)
SetTemplateVelocity(t[7], .1, .1, -.03, -.03, 0, 0)
SetTemplateGravity(t[7], .1)
SetTemplateSize(t[7], -.03, -.3)
SetTemplateFixAngles(t[7], 0, -1)

;Sounds
ts[2] = LoadSound("Media\Smoke.wav")
ts[3] = LoadSound("Media\Sparks.wav")
ts[4] = LoadSound("Media\Fire.wav")
ts[5] = LoadSound("Media\Bang.wav")
ts[6] = LoadSound("Media\Explosion.wav")
ts[7] = LoadSound("Media\Rain.wav")
End Function