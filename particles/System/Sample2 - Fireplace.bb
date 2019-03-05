Include "SampleFunctions.bb"

InitGraphics()
AmbientLight 70, 60, 50

;Camera
Cam = CreateCamera()
CameraFogMode Cam, True
CameraFogRange Cam, 0, 20
CamPiv = CreatePivot()

;Light
Global Light = CreateLight(2)
PositionEntity Light, 0, 1, 0
LightColor Light, 255, 200, 160

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
frame = frame + 2
PositionEntity Cam, Cos(frame * .1) * 6, 3.5, Sin(frame * .1) * 6
PointEntity Cam, CamPiv
LightRange Light, Rnd(15, 20)
End Function

Function CreateScene()
;Floor
c = CreatePlane(10)
EntityTexture c, LoadTexture("Media\Sand.jpg")

;Template1 - Fire
tmp1 = CreateTemplate()
SetTemplateInterval(tmp1, 1)
SetTemplateEmitterLifeTime(tmp1, -1)
SetTemplateParticleLifeTime(tmp1, 10, 25)
SetTemplateTexture(tmp1, "Media\Fire.jpg", 3, 3)
SetTemplateOffset(tmp1, -.3, .3, -.3, .3, -.3, .3)
SetTemplateVelocity(tmp1, -.04, .04, .1, .27, -.04, .04)
SetTemplateAlphaVel(tmp1, True)
SetTemplateSize(tmp1, 1.5, 1.5)
SetTemplateColors(tmp1, $FFFFFF, $FFBB00)

;Template2 - Sparks
tmp2 = CreateTemplate()
SetTemplateInterval(tmp2, 10)
SetTemplateEmitterLifeTime(tmp2, -1)
SetTemplateParticleLifeTime(tmp2, 5, 20)
SetTemplateTexture(tmp2, "Media\Spark.jpg", 2, 3)
SetTemplateOffset(tmp2, -.45, .45, .3, .5, -.45, .45)
SetTemplateVelocity(tmp2, -.065, .065, .1, .27, -.065, .065)
SetTemplateGravity(tmp2, .01)
SetTemplateAlphaVel(tmp2, False)
SetTemplateSize(tmp2, .055, .055)
SetTemplateColors(tmp2, $FFBB00, $FFFFFF)
SetTemplateBrightness(tmp2, 5)

;Woodbunch
wb = LoadMesh("Media\Woodbunch.b3d")
ScaleEntity wb, .004, .004, .004
PositionEntity wb, 0, .2, 0
SetEmitter(wb, tmp1)
SetEmitter(wb, tmp2)

;Sound
Snd_Fire = LoadSound("Media\Fire.wav")
LoopSound Snd_Fire
PlaySound Snd_Fire
End Function