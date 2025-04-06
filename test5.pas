program ForDoOOP;
type
  TLooper = class
  public
    procedure RunLoop;
  end;

procedure TLooper.RunLoop;
begin
    var j: Integer;
    j:= 100 ;    
    while j < 5 do
      begin
      WriteLn('i = ', j);      
      end;
end;

var
  Looper: TLooper;

begin
  var j: Integer;
  j:= 15 ;
  Looper := TLooper.Create;
  Looper.RunLoop;  
end.
