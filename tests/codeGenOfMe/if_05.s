.file "tests/codeGenOfMe/if_05.dcf"
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
movq $3,-26(%rbp)
movq $5,-18(%rbp)
movq $6,-10(%rbp)
movb $0,-2(%rbp)
movb $1,-1(%rbp)
movb -2(%rbp),%al
cmpb $0,%al
jle .L1
.L2:
movb -2(%rbp),%al
cmpb $0,%al
jle .L3
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L4
.L3:
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
.L4:
leave
ret
.L1:
movb -1(%rbp),%al
cmpb $0,%al
jle .L3
jmp .L2

