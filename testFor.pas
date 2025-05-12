program blah;
begin
  for j := 1 to 10 do
  begin
        if j < 5 then
        begin 
            continue;
        end;
        if j > 7 then
        begin
            break;
        end;
        WriteLn('Counter = ', j);
    end;
end.