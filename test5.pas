program continueLoop;
type
  TLooper = class
  public
    procedure RunLoop;
  end;

procedure TLooper.RunLoop;
begin
    var j: Integer ;
    j:= 0;
    while j < 10 do    
    begin
    j := j + 1;
    if j < 8 then
    begin
    continue;
    end;
    WriteLn('Counter = ', j);        
    end;
  end;



var
  Looper: TLooper;

begin  
  Looper := TLooper.Create;
  Looper.RunLoop;  
end.
