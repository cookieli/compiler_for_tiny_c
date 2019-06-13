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
subq $16, %rsp
movq $10,-16(%rbp)
movq $30,-8(%rbp)
movq -16(%rbp),%rax
cmpq $10,%rax
jg .L1
movq -8(%rbp),%rax
cmpq $40,%rax
jle .L2
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L3
.L1:
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
.L3:
leave
ret

