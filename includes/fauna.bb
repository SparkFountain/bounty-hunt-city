Dim cows(0)

Function LoadDefaultFauna()
    cows(0) = LoadSprite("gfx/fauna/cow01.png", 2)
    ScaleSprite cows(0), 3, 3
    PositionEntity cows(0), 20, TerrainY(map, 20, 0, 20) + 3, 20
End Function