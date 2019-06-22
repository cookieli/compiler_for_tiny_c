.file "tests/codeGenOfMe/bool.dcf"
.text
.section .rodata
.LC0:
.string "%d, %d\n"
.LC1:
.string "%d, %d, %d, %d, %d, %d, %dthe 8th is %d\n"
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
movsbq -1(%rbp),%rdx
movsbq -2(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
subq $8,%rsp
movq $1,%rdi
pushq %rdi
movq $3,%rdi
pushq %rdi
movq $4,%rdi
pushq %rdi
movq $3,%r9
movq $2,%r8
movq $1,%rcx
movq $1,%rdx
movq $0,%rsi
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
addq $32,%rsp
movq $0,%rax
leave
ret

