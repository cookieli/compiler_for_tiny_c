.file "tests/RegisterAlloc/reg_05.dcf"
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
movq $0,%rax
movq %rax,-16(%rbp)
movq %rax,-8(%rbp)
movb $1,%dil
cmpb $0,%dil
jle .L1
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L1
.L1:
leave
ret

