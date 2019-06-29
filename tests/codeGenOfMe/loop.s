.file "tests/codeGenOfMe/loop.dcf"
.text
.section .rodata
.LC0:
.string "hello \n"
.LC1:
.string "hi\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movq $0,-8(%rbp)
.L1:
movq -8(%rbp),%rax
cmpq $5,%rax
jge .L2
subq $16, %rsp
movq $8,-16(%rbp)
.L3:
movq -16(%rbp),%rax
cmpq $4,%rax
jle .L4
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -16(%rbp),%rax
movq $1,%r10
subq %r10,%rax
movq %rax,-16(%rbp)
jmp .L3
.L4:
movq $4,-16(%rbp)
.L5:
movq -16(%rbp),%rax
cmpq $8,%rax
jge .L6
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -16(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-16(%rbp)
jmp .L5
.L6:
movq -8(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-8(%rbp)
addq $16, %rsp
jmp .L1
.L2:
leave
ret

