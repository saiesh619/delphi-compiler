program forLoop;

type
  TCalculator = class
  private  
    Number: Integer;
  public   
    constructor Create;    
  end;


constructor TCalculator.Create;
begin
  WriteLn('Constructor called: Number initialized to 0');     
end;  

var
  Calc: TCalculator;
begin
  Calc := TCalculator.Create;   
  
end.
