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
        ScaleEntity map, 1, 10, 1
        PositionEntity map, 20, 0, 20
    EndIf

    ; TODO: change color map path
    mapTexture = LoadTexture("maps/" + name + "/heightmap.png")
    ScaleTexture mapTexture, 256, 256
    EntityTexture map, mapTexture

    TerrainShading map, True
    TerrainDetail map, 10000
End Function

Function InitTestMap()
    Local car.T_Car = New T_Car
    Local carDef.T_Vehicle_Definition = First T_Vehicle_Definition
    car\entity = CopyEntity(carDef\entity) : ShowEntity car\entity
    car\speed = 0
    car\maxSpeed = carDef\maxSpeed
    car\energy = 5
    car\maxEnergy# = carDef\energy
End Function

Function AnimateWater()
    Local motionFactor# = Sin((ms / 10) Mod 360)

    ; ScaleEntity water, 10 + 0.2 * motionFactor, 1, 10 + 0.2 * motionFactor
    ; TurnEntity water, 0, 0.02 * motionFactor, 0
    MoveEntity water, 0.003, 0, 0.005
End Function