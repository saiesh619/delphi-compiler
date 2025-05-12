define i32 @Add() {
 %t0 = alloca i32
 %t1 = alloca i32
 store i32 0, i32* %t1
 store i32 8, i32* %t1
 %t2 = load i32, i32* %t1
 store i32 %t2, i32* %t0
 %t3 = load i32, i32* %t0
 ret i32 %t3
}


@.str5 = constant [10 x i8] c"Result = \00"
; ModuleID = 'main'
declare i32 @printf(i8*, ...)
declare i32 @read_int()
@.str = constant [4 x i8] c"%d\0A\00"

define i32 @main() {
 %t4 = alloca i32
 store i32 0, i32* %t4
 call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([10 x i8], [10 x i8]* @.str5, i32 0, i32 0))
 %t6 = call i32 @Add()
 call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str, i32 0, i32 0), i32 %t6)
 ret i32 0
}
