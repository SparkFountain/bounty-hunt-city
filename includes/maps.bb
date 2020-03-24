; TODO: use several water layers with transparency
Global water = CreatePlane()
EntityTexture water, LoadTexture("gfx/water.png")
ScaleEntity water, 10, 1, 10
EntityShininess water, 0.9
EntityAlpha water, 0.5

Global map, mapTexture

Function LoadMap(name$)
    Local heightmapExists = FileType("maps/" + name + "/heightmap.png")
    If heightmapExists = 0 Then
        RuntimeError "Heightmap does not exist for map '" + name + "'"
    ElseIf heightmapExists = 1 Then
        map = LoadTerrain("maps/" + name + "/heightmap.png")
        ScaleEntity map, 1, 3, 1
        PositionEntity map, -128, 0, -128
    EndIf

    mapTexture = LoadTexture("maps/" + name + "/colormap.png")
    ScaleTexture mapTexture, 256, 256
    EntityTexture map, mapTexture

    TerrainShading map, True
    TerrainDetail map, 10000
End Function

Function InitTestMap()
    Local car.T_Car = New T_Car
    Local carDef.T_Vehicle_Definition = First T_Vehicle_Definition
    car\entity = CopyEntity(carDef\entity) : ShowEntity car\entity
    car\minSpeed = carDef\minSpeed
    car\maxSpeed = carDef\maxSpeed
    car\acceleration = carDef\acceleration
    car\friction = carDef\friction
    car\energy = 5
    car\maxEnergy# = carDef\energy
    car\engineSound = carDef\engineSound
    car\hornSound = carDef\hornSound

    If Not ChannelPlaying(ambientChannel) Then ambientChannel = PlaySound(soundAmbient(Rand(0,1)))
End Function

Function AnimateWater()
    Local motionFactor# = Sin((ms / 10) Mod 360)

    ; ScaleEntity water, 10 + 0.2 * motionFactor, 1, 10 + 0.2 * motionFactor
    ; TurnEntity water, 0, 0.02 * motionFactor, 0
    MoveEntity water, 0.003, 0, 0.005
End Function