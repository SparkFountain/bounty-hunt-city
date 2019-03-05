Include "SampleFunctions.bb"

InitGraphics()
AmbientLight 70, 60, 50

;Globs
Type Rocket
	Field ent, speed#, age, max_age
End Type
Type ExplosionSound
	Field wait
End Type

;Camera
Cam = CreateCamera()
PositionEntity Cam, 0, 0, -10

;Backdrop
c = CreateSprite()
PositionEntity c, 0, 0, 60
ScaleSprite c, 80, 60
EntityTexture c, LoadTexture("Media\Backdrop.jpg")

;Light
RotateEntity CreateLight(), 45, 45, 0

;Particles
InitParticles(Cam)

;Scene
CreateScene()

While Not KeyHit(1)
	UpdateProgram(Cam)
Wend
FreeParticles()
End

Function UpdateSample()
If Rand(0, 15) = 0 Then
	cnt = 0
	For r.Rocket = Each Rocket
		cnt = cnt + 1
	Next
	If cnt < 3 Then
		r.Rocket = New Rocket
		r\ent = CreateCylinder(5)
		ScaleEntity r\ent, .06, 1, .06
		PositionEntity r\ent, Rnd(-15, 15), -15, Rnd(10, 12)
		EntityColor r\ent, 255, 0, 0
		SetEmitter(r\ent, tmp1)
		r\speed# = .01
		r\max_age = Rand(45, 60)
		RotateEntity r\ent, Rnd(-30, 30), 0, 0
		SoundPitch Snd_RocketStartUp, Rand(50000, 90000)
		PlaySound Snd_RocketStartUp
	EndIf
EndIf
For r.Rocket = Each Rocket
	r\age = r\age + 1
	If r\age < 10 Then r\speed# = r\speed# * 1.5
	MoveEntity r\ent, 0, r\speed#, 0
	If r\age > r\max_age Then
		FreeEmitter(r\ent, False)
		SetEmitter(r\ent, tmp2, True)
		FreeEntity r\ent
		Delete r
		For i = 1 To 10
			s.ExplosionSound = New ExplosionSound
			s\wait = Rand(0, 20)
		Next
	EndIf
Next
For s.ExplosionSound = Each ExplosionSound
	If s\wait < 0 Then
		ch = PlaySound(Snd_Explosion)
		ChannelVolume ch, .5
		ChannelPitch ch, Rand(1000, 70000)
		Delete s
	Else
		s\wait = s\wait - 1
	EndIf
Next
End Function

Function PrintDebugBar(update_particles)
cnt_particles = 0
For p.Particle = Each Particle
	cnt_particles = cnt_particles + 1
Next
cnt_emitters = 0
For e.Emitter = Each Emitter
	cnt_emitters = cnt_emitters + 1
Next
Text 0, 0, "Update particles: " + update_particles + " ms"
Text 0, 15, "Particles: " + cnt_particles
Text 0, 30, "Emitters: " + cnt_emitters
Text 0, 45, "TrisRendered: " + TrisRendered()
End Function

Global tmp1, tmp2
Global Snd_RocketStartUp, Snd_Explosion
Function CreateScene()

;Template1 - Smoke
tmp1 = CreateTemplate()
SetTemplateEmitterBlend(tmp1, 1)
SetTemplateInterval(tmp1, 1)
SetTemplateEmitterLifeTime(tmp1, 70)
SetTemplateParticleLifeTime(tmp1, 50, 65)
SetTemplateTexture(tmp1, "Media\Smoke.png", 2)
SetTemplateOffset(tmp1, -.1, .1, -1.1, -.9, -.1, .1)
SetTemplateVelocity(tmp1, -.01, .01, -.01, .01, -.01, .01)
SetTemplateRotation(tmp1, -3, 3)
SetTemplateAlpha(tmp1, .3)
SetTemplateAlphaVel(tmp1, True)
SetTemplateSize(tmp1, 1.5, 1.5, .7, 1.5)
;Smokefire
t0 = CreateTemplate()
SetTemplateEmitterBlend(t0, 3)
SetTemplateInterval(t0, 1)
SetTemplateParticlesPerInterval(t0, 3)
SetTemplateEmitterLifeTime(t0, -1)
SetTemplateParticleLifeTime(t0, 5, 10)
SetTemplateTexture(t0, "Media\Fire.jpg", 3)
SetTemplateOffset(t0, -.1, .1, -1.1, -.9, -.1, .1)
SetTemplateVelocity(t0, -.01, .01, -.01, .01, -.01, .01)
SetTemplateRotation(t0, -3, 3)
SetTemplateAlphaVel(t0, True)
SetTemplateSize(t0, .5, .5, .7, 1.5)
SetTemplateBrightness(t0, 3)
SetTemplateSubTemplate(tmp1, t0)

;Template2 - Explosion
tmp2 = CreateTemplate()
SetTemplateEmitterBlend(tmp2, 3)
SetTemplateInterval(tmp2, 1)
SetTemplateParticlesPerInterval(tmp2, 30)
SetTemplateEmitterLifeTime(tmp2, 1)
SetTemplateParticleLifeTime(tmp2, 70, 85)
SetTemplateTexture(tmp2, "Media\Spark.jpg", 3)
SetTemplateOffset(tmp2, -.2, .2, -.2, .2, -.2, .2)
SetTemplateVelocity(tmp2, -.1, .1, -.1, .2, -.1, .1)
SetTemplateAlignToFall(tmp2, True, 45)
SetTemplateGravity(tmp2, .0015)
SetTemplateAlphaVel(tmp2, True)
SetTemplateSize(tmp2, .45, .1)
SetTemplateColors(tmp2, $FF0000, $FF0000)
SetTemplateBrightness(tmp2, 8)
;Blue explosion
t0 = CreateTemplate()
SetTemplateEmitterBlend(t0, 3)
SetTemplateInterval(t0, 1)
SetTemplateParticlesPerInterval(t0, 30)
SetTemplateEmitterLifeTime(t0, 1)
SetTemplateParticleLifeTime(t0, 70, 85)
SetTemplateTexture(t0, "Media\Spark.jpg", 3)
SetTemplateOffset(t0, -.2, .2, -.2, .2, -.2, .2)
SetTemplateVelocity(t0, -.1, .1, -.1, .2, -.1, .1)
SetTemplateAlignToFall(t0, True, 45)
SetTemplateGravity(t0, .0015)
SetTemplateAlphaVel(t0, True)
SetTemplateSize(t0, .45, .1)
SetTemplateColors(t0, $0000FF, $0000FF)
SetTemplateBrightness(t0, 8)
SetTemplateSubTemplate(tmp2, t0)
;Yellow
t0 = CreateTemplate()
SetTemplateEmitterBlend(t0, 3)
SetTemplateInterval(t0, 1)
SetTemplateParticlesPerInterval(t0, 70)
SetTemplateEmitterLifeTime(t0, 1)
SetTemplateParticleLifeTime(t0, 70, 85)
SetTemplateTexture(t0, "Media\Spark.jpg", 3)
SetTemplateOffset(t0, -.2, .2, -.2, .2, -.2, .2)
SetTemplateVelocity(t0, -.05, .05, -.03, .1, -.05, .05)
SetTemplateGravity(t0, .00125)
SetTemplateAlphaVel(t0, True)
SetTemplateSize(t0, .3, .3)
SetTemplateBrightness(t0, 3)
SetTemplateSubTemplate(tmp2, t0)

;Sounds
Snd_RocketStartUp = LoadSound("Media\RocketStartUp.wav")
Snd_Explosion = LoadSound("Media\Explosion2.wav")
End Function