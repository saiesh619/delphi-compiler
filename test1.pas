program NumberIO;

type
  TCalculator = class
  private  
    Number: Integer;
  public   
    constructor Create;
    procedure ReadNumber;
    procedure ShowNumber;
    destructor Destroy;
  end;

procedure TCalculator.ReadNumber;
begin
  Write('Enter a number: ');
  ReadLn(Number);
end;

procedure TCalculator.ShowNumber;
begin
  WriteLn('Your number is: ', Number);
end;

var
  Calc: TCalculator;
begin  
  Calc := TCalculator.Create;
  Write('I am before reading ');
  Calc.ReadNumber;
  Write('I am after reading ');
  Calc.ShowNumber;
end.
