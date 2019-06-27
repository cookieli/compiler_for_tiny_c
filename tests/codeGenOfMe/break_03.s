.file "tests/codeGenOfMe/break_03.dcf"
.text
.section .rodata
.LC0:
.string "hello world\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movq $6,-8(%rbp)
movq -8(%rbp),%rax
cmpq $2,%rax
jne .L1
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L1
.L1:
nop
.L2:
movq -8(%rbp),%rax
cmpq $0,%rax
jle .L3
movq -8(%rbp),%rax
movq $1,%r10
subq %r10,%rax
movq %rax,-8(%rbp)
jmp .L2
.L3:
leave
ret

