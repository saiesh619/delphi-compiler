@.str8 = constant [11 x i8] c"Counter = \00"; ModuleID = 'main'
declare i32 @printf(i8*, ...)
declare i32 @read_int()
@.str = constant [4 x i8] c"%d\0A\00"

define i32 @main() {
  %t0 = alloca i32
  store i32 0, i32* %t0
  br label %label0
  label0:
  %t1 = load i32, i32* %t0
  %t2 = icmp slt i32 %t1, 10
  br i1 %t2, label %label1, label %label2
  label1:
  %t3 = load i32, i32* %t0
  %t4 = add i32 0, 1
  %t5 = add i32 %t3, %t4
  store i32 %t5, i32* %t0
  %t6 = load i32, i32* %t0
  %t7 = icmp slt i32 %t6, 5
  br i1 %t7, label %label3, label %label4
  label3:
  br label %label0
  br label %label4
  label4:
  call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([11 x i8], [11 x i8]* @.str8, i32 0, i32 0))
  %t8 = load i32, i32* %t0
  call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str, i32 0, i32 0), i32 %t8)
  br label %label0
  label2:
  ret i32 0
}
