Function GetEntityDistance#(entity1,entity2)
	Return Sqr(Power(EntityX(entity1) - EntityX(entity2)) + Power(EntityY(entity1) - EntityY(entity2)) + Power(EntityZ(entity1) - EntityZ(entity2)))
End Function

Function GetDistance2D#(x1#, z1#, x2#, z2#)
	Return Sqr(Power(x1-x2) + Power(z1-z2))
End Function

Function Power#(value#, exponent=2)
	Local result# = value
	For i=2 To exponent
		result = result*value
	Next
	Return result
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D