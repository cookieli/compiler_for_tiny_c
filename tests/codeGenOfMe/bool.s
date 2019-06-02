.file "tests/codeGenOfMe/bool.dcf"
.text
.section .rodata
.LC0:
.string "%d, %d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movb $1,-2(%rbp)
movb -2(%rbp),%al
movb %al,-1(%rbp)
movzbq -1(%rbp),%rdx
movzbq -2(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret

