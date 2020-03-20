; This code has been rewritten based on a Python JSON Parser:
; https://github.com/eatonphil/pj

Const JSON_COMMA = ","
Const JSON_COLON = ":"
Const JSON_LEFTBRACKET = "["
Const JSON_RIGHTBRACKET = "]"
Const JSON_LEFTBRACE = "{"
Const JSON_RIGHTBRACE = "}"
Const JSON_QUOTE = Chr(34)

Const JSON_QUOTE = """
Const JSON_WHITESPACE = [" ", "\t", "\b", "\n", "\r"]
Const JSON_SYNTAX = [JSON_COMMA, JSON_COLON, JSON_LEFTBRACKET, JSON_RIGHTBRACKET, JSON_LEFTBRACE, JSON_RIGHTBRACE]

Const FALSE_LEN = Len("False")
Const TRUE_LEN = Len("True")
Const NULL_LEN = Len("Null")


Function from_string(json):
	Local tokens = lex(json)
	Return parse(tokens, is_root=True)[0]
End Function

Function to_string(json):
	Local json_type = Type(json)
    If json_type is dict:
        String = "{"
        dict_len = Len(json)
		
        For i, (key, val) in enumerate(json.items()):
            String += ""{}": {}".format(key, to_string(val))
			
            If i < dict_len - 1:
                String += ", "
				Else:
                String += "}"
				
				Return String
			ElseIf json_type is list:
				String = "["
				list_len = Len(json)
				
				For i, val in enumerate(json):
					String += to_string(val)
					
					If i < list_len - 1:
						String += ", "
						Else:
						String += "]"
						
						Return String
						elif json_type is Str:
						Return ""{}"".format(json)
						elif json_type is bool:
						Return "True" If json Else "False"
							elif json_type is None:
							Return "Null"
							
							Return Str(json)
End Function

Function lex_string(String):
	json_string = ""

	If String[0] == JSON_QUOTE Then
		String = String[1:]
    Else
		Return None, String
	EndIf
	
    For c in String:
        If c == JSON_QUOTE:
            Return json_string, String[Len(json_string)+1:]
			Else:
            json_string += c
			
			RuntimeError("Expected End-of-String quote")
		EndIf
	Next
End Function









Function ParseJSON(file$, category$)
	
	
	Local keyFile = ReadFile(file)
	While Not Eof(keyFile)
		Local char = Chr(ReadByte(keyFile))
		
		
		Print 
	Wend
	CloseFile keyFile
End Function

Type T_Property
	Field category$
	Field key$
	Field value$
End Type
;~IDEal Editor Parameters:
;~C#Blitz3D