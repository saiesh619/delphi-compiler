program NumberIO;

type
  TCalculator = class
  private  
    Number: Integer;
  public   
    constructor Create;
    procedure ReadNumber;
    function Add; 
  end;

procedure TCalculator.ReadNumber;
begin
  var x: integer;
  x := 100;
  Writeln('Start: x = ', x);

  while x > 90 do
  begin
    var x: integer;
    x := 50;
    Writeln('Inside WHILE: x = ', x);

    if x < 60 then
    begin
      var x: integer;
      x := 25;
      Writeln('Inside IF (x < 60): x = ', x);
    end;

    Writeln('After IF, in WHILE: x = ', x);

    break;
  end;

  Writeln('After WHILE: x = ', x);
end;

function TCalculator.Add;
  begin
   var j: Integer ;
    j:= 0;
    while j < 10 do    
    begin
    j := j + 1;
    if j > 5 then
    begin 
    continue;
    end;
    WriteLn('Counter = ', j);        
    end;
  
  result := j; 
end;

var
  Calc: TCalculator;
begin  
  Calc := TCalculator.Create;
  Writeln('Function call = ', Calc.Add());
  Writeln('Procedure call');
  Calc.ReadNumber
end.
