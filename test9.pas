program Number;

type
  TLooper = class
  public
    function funcAdd;
  end;

function TLooper.funcAdd;
begin
  var result: Integer;
  WriteLn('Inside the function called');
  result:= 78 + 12;
end;

var
  Looper: TLooper;

begin  
  Looper := TLooper.Create;
  WriteLn('Before callling function');
  WriteLn('Answer: ',  Looper.funcAdd());
end.
