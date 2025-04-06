program ForDoOOP;
type
  TLooper = class
  public
    procedure RunLoop;
  end;

procedure TLooper.RunLoop;
begin
    var j: Integer ;
    j:= 120;
    while j < 5 do    
    begin    
    continue;
    WriteLn('!!!!!!!!!!!!!!!!!!!!!!!!', i);    
    i := i + 1;
  end;

end;

var
  Looper: TLooper;

begin
  var j: Integer;
  j:= 115 ;
  Looper := TLooper.Create;
  Looper.RunLoop;  
end.
