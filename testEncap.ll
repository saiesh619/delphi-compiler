%class.TCounter = type { i32 }
define void @TCounter_SetValue(%class.TCounter* %self) {
 %t0 = getelementptr %class.TCounter, %class.TCounter* %self, i32 0, i32 0
 store i32 0, i32* %t0
 ret void
}

define void @TCounter_Increment(%class.TCounter* %self) {
 %t1 = getelementptr %class.TCounter, %class.TCounter* %self, i32 0, i32 0
 %t2 = load i32, i32* %t1
 %t3 = add i32 %t2, 1
 %t4 = getelementptr %class.TCounter, %class.TCounter* %self, i32 0, i32 0
 store i32 %t3, i32* %t4
 ret void
}

define i32 @TCounter_GetValue(%class.TCounter* %self) {
 %t5 = alloca i32
 %t6 = getelementptr %class.TCounter, %class.TCounter* %self, i32 0, i32 0
 %t7 = load i32, i32* %t6
 store i32 %t7, i32* %t5
 %t8 = load i32, i32* %t5
 ret i32 %t8
}


@.str10 = constant [11 x i8] c"Counter = \00"
; ModuleID = 'main'
declare i32 @printf(i8*, ...)
declare i32 @read_int()
@.str = constant [4 x i8] c"%d\0A\00"

define i32 @main() {
 %t9 = alloca %class.TCounter
 call i32 @TCounter_SetValue(%class.TCounter* %t9)
 call i32 @TCounter_Increment(%class.TCounter* %t9)
 call i32 @TCounter_Increment(%class.TCounter* %t9)
 call i32 @TCounter_Increment(%class.TCounter* %t9)
 call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([11 x i8], [11 x i8]* @.str10, i32 0, i32 0))
 %t11 = call i32 @TCounter_GetValue(%class.TCounter* %t9)
 call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str, i32 0, i32 0), i32 %t11)
 ret i32 0
}
