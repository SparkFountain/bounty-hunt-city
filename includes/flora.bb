Dim trees(5)
Dim palms(1)

Function LoadDefaultFlora()    
    Local i
    For i=0 To 5
        trees(i) = LoadSprite("gfx/flora/tree0"+(i+1)+".png", 2)
        ScaleSprite trees(i), 7, 7
        PositionEntity trees(i), i*20+10, TerrainY(map, i*20+10, 0, 0) + 3, 0
    Next
End Function

Function LoadFlora()

End Function