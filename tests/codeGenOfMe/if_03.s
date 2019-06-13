.file "tests/codeGenOfMe/if_03.dcf"
.text
.section .rodata
.LC0:
.string "hello world\n"
.LC1:
.string "yes the world\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $32, %rsp
movq $10,-24(%rbp)
movq $30,-16(%rbp)
movq $-40,-8(%rbp)
movq -24(%rbp),%rax
cmpq $10,%rax
jg .L1
movq -16(%rbp),%rax
cmpq $40,%rax
jge .L1
movq -24(%rbp),%rax
movq -8(%rbp),%r10
cmpq %r10,%rax
jl .L1
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L2
.L1:
movq -24(%rbp),%rax
movq -16(%rbp),%r10
cmpq %r10,%rax
jge .L3
movq -8(%rbp),%rax
cmpq $2,%rax
jle .L3
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L2
.L3:
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
.L2:
leave
ret

