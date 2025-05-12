program SampleProcedureCall;

procedure SayHellos;
begin
  var j: Integer ;
    j:= 0;
    while j < 10 do    
    begin
    j := j + 1;        
    WriteLn('Counter = ', j);        
    end;
end;

begin
  Writeln('Program started.');
  SayHellos; 
  Writeln('Program ended.');
end.