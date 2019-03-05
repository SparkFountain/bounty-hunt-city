Include "SampleFunctions.bb"

InitGraphics()

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
End Function

Function CreateScene()
;Template - Explosion
tmp = CreateTemplate()
SetTemplateInterval(tmp, 39)
SetTemplateEmitterLifeTime(tmp, -1)
SetTemplateParticleLifeTime(tmp, 39, 39)
SetTemplateAnimTexture(tmp, "Media\BoomStrip.png", 1, 1, 64, 64, 39)
SetTemplateOffset(tmp, -3, 3, -3, 3, -3, 3)
SetTemplateVelocity(tmp, -.04, .04, -.04, .04, -.04, .04)
SetTemplateSize(tmp, 3, 3)

;Emitter
c = CreatePivot()
PositionEntity c, 0, 0, 10
SetEmitter(c, tmp)
End Function