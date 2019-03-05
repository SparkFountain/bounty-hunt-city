Include "DevilParticleSystem.bb"

Global font_height = FontHeight() + 3
Global render_mode = 1
Global Cam, CamPiv, frame
Global Img_Logo

Function InitGraphics(w = 1024, h = 768)
Graphics3D w, h, 32, 2
SetBuffer BackBuffer()
SeedRnd MilliSecs()
Img_Logo = LoadImage("Media\DPS_Logo.png")
MidHandle Img_Logo
Cam = CreateCamera()
CameraRange Cam, .1, 10000
CamPiv = CreatePivot()
End Function

Function StartProgram(center_mouse = False)
If center_mouse Then MoveMouse GraphicsWidth() / 2, GraphicsHeight() / 2
FlushMouse()
FlushKeys()
End Function

Const framePeriod = 13
Global frameTime = MilliSecs() - framePeriod
Global updateparticle_time
Function UpdateProgram(Cam, txt$ = "")
Repeat
	frameElapsed = MilliSecs() - frameTime
Until frameElapsed
frameTicks = frameElapsed / framePeriod
;Render
gw = GraphicsWidth()
gh = GraphicsHeight()
h = 90
ho = gh - h
CameraViewport Cam, 0, h, gw, gh - h * 2
Cls
ms = MilliSecs()
renderparticles_time = MilliSecs() - ms
RenderWorld
Color 0, 0, 0
Rect 0, 0, gw, h
Rect 0, gh - h, gw, h
Color 255, 255, 255
render_time = MilliSecs() - ms
cnt_particles = 0
For p.Particle = Each Particle
	cnt_particles = cnt_particles + 1
Next
cnt_emitters = 0
For e.Emitter = Each Emitter
	cnt_emitters = cnt_emitters + 1
Next
Text 10, ho + 10, "FPS: " + GetFPS()
Text 10, ho + 25, "Render time: " + render_time
Text 10, ho + 40, "UpdateParticle time: " + updateparticle_time
If txt$ <> "" Then Text gw - StringWidth(txt$) - 10, gh - h + 10, txt
DrawImage Img_Logo, gw / 2, h / 2
Flip 0
;Update
updateparticle_time = 0
If frameTicks > 4 Then frameTicks = 4
For frameLimit = 1 To frameTicks
	If frameLimit = frameTicks Then CaptureWorld
	frameTime = frameTime + framePeriod
	UpdateSample()
	ms = MilliSecs()
	UpdateParticles()
	updateparticle_time = updateparticle_time + MilliSecs() - ms
Next
End Function

Global FPS, FPS_temp, FPS_time
Function GetFPS()
ctime = MilliSecs()
FPS_temp = FPS_temp + 1
If ctime - FPS_time > 500 Then
	FPS = FPS_temp * 2
	FPS_temp = 0
	FPS_time = ctime
EndIf
Return FPS
End Function