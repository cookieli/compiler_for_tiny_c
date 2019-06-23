.file "tests/codeGenOfMe/BoundCheck_04.dcf"
.text
.section .rodata
.LC0:
.string "tests/codeGenOfMe/BoundCheck_04.dcf:7:8:out of array bound error for a\n"
.LC1:
.string "tests/codeGenOfMe/BoundCheck_04.dcf:11:10:out of array bound error for a\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $128, %rsp
movq $12,-128(%rbp)
movq $0,-24(%rbp)
.L1:
movq -24(%rbp),%rax
movq -128(%rbp),%r10
cmpq %r10,%rax
jge .L2
movq -24(%rbp),%rax
cmpq $12,%rax
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
movq -24(%rbp),%rax
movq -24(%rbp),%r10
movq %r10,-120(%rbp,%rax,8)
movq -24(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-24(%rbp)
jmp .L1
.L2:
movq $0,-24(%rbp)
.L4:
movq -24(%rbp),%rax
movq $2,%r10
addq %r10,%rax
movq %rax,-16(%rbp)
movq -16(%rbp),%rax
cmpq $12,%rax
jl .L5
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq $-1,%rdi
movq $0,%rax
call exit@PLT
movq $0,%rax
.L5:
movq -16(%rbp),%rax
movq -120(%rbp,%rax,8),%r10
movq %r10,-8(%rbp)
movq -8(%rbp),%rax
cmpq $100,%rax
jge .L6
movq -24(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-24(%rbp)
jmp .L4
.L6:
leave
ret
jmp .L5

