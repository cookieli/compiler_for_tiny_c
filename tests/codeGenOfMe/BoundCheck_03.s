.file "tests/codeGenOfMe/BoundCheck_03.dcf"
.text
.section .rodata
.LC0:
.string "tests/codeGenOfMe/BoundCheck_03.dcf:6:4:out of array bound error for a\n"
.LC1:
.string "tests/codeGenOfMe/BoundCheck_03.dcf:9:6:out of array bound error for a\n"
.LC2:
.string "tests/codeGenOfMe/BoundCheck_03.dcf:9:16:out of array bound error for a\n"
.LC3:
.string "%d"
.LC4:
.string "tests/codeGenOfMe/BoundCheck_03.dcf:9:27:out of array bound error for a\n"
.LC5:
.string "tests/codeGenOfMe/BoundCheck_03.dcf:9:27:out of array bound error for a\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $112, %rsp
movq $9,-96(%rbp)
movq $0,-104(%rbp)
.L1:
movq -104(%rbp),%rax
movq -96(%rbp),%r10
cmpq %r10,%rax
jge .L2
movq -104(%rbp),%rax
cmpq $9,%rax
jl .L3
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L3
.L3:
movq -104(%rbp),%rax
movq $0,-88(%rbp,%rax,8)
movq -104(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-104(%rbp)
jmp .L1
.L2:
movq $0,-104(%rbp)
movq -104(%rbp),%rax
cmpq $9,%rax
jl .L4
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L4
.L4:
movq -104(%rbp),%rax
movq $0,-88(%rbp,%rax,8)
.L5:
movq -104(%rbp),%rax
cmpq $9,%rax
jl .L6
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
.L6:
movq -104(%rbp),%rax
movq -88(%rbp,%rax,8),%r10
movq %r10,-16(%rbp)
movq -16(%rbp),%rax
cmpq $20,%rax
jge .L7
movq -104(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-104(%rbp)
movq -104(%rbp),%rsi
leaq .LC3(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -104(%rbp),%rax
cmpq $9,%rax
jl .L8
leaq .LC4(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L8
.L8:
movq -104(%rbp),%rax
movq -88(%rbp,%rax,8),%r10
movq %r10,-8(%rbp)
movq -8(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-8(%rbp)
movq -104(%rbp),%rax
cmpq $9,%rax
jl .L9
leaq .LC5(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
jmp .L9
.L9:
movq -104(%rbp),%rax
movq -8(%rbp),%r10
movq %r10,-88(%rbp,%rax,8)
jmp .L5
.L7:
leave
ret
jmp .L6

