.file "tests/codeGenOfMe/func_02.dcf"
.text
.comm	a,64,32
.section .rodata
.LC0:
.string "%d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $32, %rsp
movq $8,a(%rip)
movq $5,-32(%rbp)
movq $2,-24(%rbp)
movq $6,-16(%rbp)
movq $0,%rax
call rand@PLT
leaq a+8(%rbp),%r10
movq -32(%rbp),%r11
movq %rax,(%r10,%r11,8)
leaq a+8(%rbp),%rax
movq -32(%rbp),%r10
movq (%rax,%r10,8),%r11
movq %r11,-8(%rbp)
movq -8(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret

