Include "SampleFunctions.bb"

InitGraphics()

;Globs
Dim Water(50, 50, 1)

;Camera
Cam = CreateCamera()
CameraFogMode Cam, True
CameraFogRange Cam, 10, 20
CameraFogColor Cam, 211, 219, 236
CamPiv = CreatePivot()
PositionEntity CamPiv, 0, 1, 0

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
frame = frame + 3
PositionEntity Cam, Cos(frame * .1) * 6, 4, Sin(frame * .1) * 6
PointEntity Cam, CamPiv
UpdateWater()
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

Function CreateScene()
;Fountain
c = LoadMesh("Media\Fountain.b3d")
ScaleEntity c, .01, .01, .01

;Water
CreateWater()

;Grass
c = CreatePlane()
grass_tex = LoadTexture("Media\Grass.jpg")
ScaleTexture grass_tex, 2.5, 2.5
EntityTexture c, grass_tex

;Template1 - Fountain
tmp1 = CreateTemplate()
SetTemplateEmitterBlend(tmp1, 1)
SetTemplateInterval(tmp1, 1)
SetTemplateParticlesPerInterval(tmp1, 2)
SetTemplateEmitterLifeTime(tmp1, -1)
SetTemplateParticleLifeTime(tmp1, 20, 30)
SetTemplateTexture(tmp1, "Media\Water.png", 3, 3)
SetTemplateOffset(tmp1, 0, 0, 2.7, 2.7, 0, 0)
SetTemplateVelocity(tmp1, -.07, .07, .16, .16, -.07, .07)
SetTemplateGravity(tmp1, .02)
SetTemplateAlphaVel(tmp1, True)
SetTemplateSize(tmp1, .02, .02, .8, 1.3)
SetTemplateSizeVel(tmp1, .03, 1.1)
SetTemplateColors(tmp1, $AAAAFF, $6565FF)

;Template2 - Splash1
tmp2 = CreateTemplate()
SetTemplateEmitterBlend(tmp2, 1)
SetTemplateInterval(tmp2, 1)
SetTemplateParticlesPerInterval(tmp2, 2)
SetTemplateEmitterLifeTime(tmp2, -1)
SetTemplateParticleLifeTime(tmp2, 20, 30)
SetTemplateTexture(tmp2, "Media\WaterHit.png", 3, 3)
SetTemplateOffset(tmp2, -2.5, 2.5, 1, 1, -2.5, 2.5)
SetTemplateAlphaVel(tmp2, True)
SetTemplateSize(tmp2, .02, .02, .8, 1.3)
SetTemplateSizeVel(tmp2, .03, 1)
SetTemplateFixAngles(tmp2, 90, -1)

;Template3 - Splash2
tmp3 = CreateTemplate()
SetTemplateEmitterBlend(tmp3, 1)
SetTemplateInterval(tmp3, 1)
SetTemplateEmitterLifeTime(tmp3, -1)
SetTemplateParticleLifeTime(tmp3, 20, 30)
SetTemplateTexture(tmp3, "Media\Rain.png", 3, 3)
SetTemplateOffset(tmp3, -2.5, 2.5, 1, 1, -2.5, 2.5)
SetTemplateVelocity(tmp3, 0, 0, .03, .07, 0, 0)
SetTemplateGravity(tmp3, .01)
SetTemplateAlphaVel(tmp3, False)
SetTemplateSize(tmp3, .02, .07)
SetTemplateFixAngles(tmp3, 0, -1)

;Emitters
p = CreatePivot()
SetEmitter(p, tmp1)
SetEmitter(p, tmp2)
SetEmitter(p, tmp3)

;Sound
Snd_Water = LoadSound("Media\Fountain.wav")
LoopSound Snd_Water
PlaySound Snd_Water
End Function

Global WaterSurf
Function CreateWater()
size = 50
WaterMesh = CreateMesh()
ScaleEntity WaterMesh, .5, .027, .5
PositionEntity WaterMesh, 0, .99, 0
water_tex = LoadTexture("Media\Water.jpg")
ScaleTexture water_tex, 10, 10
EntityTexture WaterMesh, water_tex
EntityAlpha WaterMesh, .6
WaterSurf = CreateSurface(WaterMesh)
For x = 0 To size
	For z = 0 To size
		Water(x, z, 0) = AddVertex(WaterSurf, x * .5 - size * .25, 0, z * .5 - size * .25, x, z, z)
		Water(x, z, 1) = Rand(0, 360)
	Next
Next
For x = 0 To size - 1
	For z = 0 To size - 1
		dis# = Sqr((x * .5 - size * .25) ^ 2 + (z * .5 - size * .25) ^ 2)
		If dis# < 7.7 Then
			AddTriangle WaterSurf, Water(x, z, 0), Water(x, z + 1, 0), Water(x + 1, z, 0)
			AddTriangle WaterSurf, Water(x + 1, z, 0), Water(x, z + 1, 0), Water(x + 1, z + 1, 0)
		EndIf
	Next
Next
End Function

Function UpdateWater()
For x = 1 To 50
	For z = 1 To 50
		Water(x, z, 1) = Water(x, z, 1) + 6
		VertexCoords WaterSurf, Water(x, z, 0), VertexX(WaterSurf, Water(x, z, 0)), Sin(Water(x, z, 1)), VertexZ(WaterSurf, Water(x, z, 0))
	Next
Next
End Function