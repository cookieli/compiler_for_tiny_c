.file "tests/codeGenOfMe/arrLen.dcf"
.text
.comm	e,7104,32
.comm	h,999,32
.section .rodata
.LC0:
.string "%d, %d\n"
.LC1:
.string "%d, %d\n"
.LC2:
.string "%d, %d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $112, %rsp
movq $888,e(%rip)
movq $999,h(%rip)
movq $7,-98(%rbp)
movq $10,-34(%rbp)
movq $6,-90(%rbp)
movq $9,-82(%rbp)
movq $0,-74(%rbp)
movb $1,-22(%rbp)
movq -98(%rbp),%rax
movq %rax,-16(%rbp)
movq -34(%rbp),%rax
movq %rax,-8(%rbp)
movq -8(%rbp),%rdx
movq -16(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq -98(%rbp),%rax
movq -34(%rbp),%r10
addq %r10,%rax
movq %rax,-8(%rbp)
movq -8(%rbp),%rdx
movq -16(%rbp),%rsi
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq e(%rip),%rax
movq %rax,-16(%rbp)
movq h(%rip),%rax
movq %rax,-8(%rbp)
movq -8(%rbp),%rdx
movq -16(%rbp),%rsi
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
leave
ret

